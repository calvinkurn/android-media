package com.tokopedia.abstraction.common.network.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * @author Angga.Prasetiyo on 27/11/2015.
 */
public class TkpdAuthInterceptor extends TkpdBaseInterceptor {
    private static final int ERROR_FORBIDDEN_REQUEST = 403;
    private static final String ACTION_TIMEZONE_ERROR = "com.tokopedia.tkpd.TIMEZONE_ERROR";

    private static final String REQUEST_METHOD_GET = "GET";
    private static final String REQUEST_METHOD_POST = "POST";
    private static final String REQUEST_METHOD_PATCH = "PATCH";
    private static final String REQUEST_METHOD_DELETE = "DELETE";

    private static final String RESPONSE_STATUS_OK = "OK";
    private static final String RESPONSE_STATUS_FORBIDDEN = "FORBIDDEN";
    private static final String RESPONSE_STATUS_UNDER_MAINTENANCE = "UNDER_MAINTENANCE";
    private static final String RESPONSE_STATUS_REQUEST_DENIED = "REQUEST_DENIED";
    private static final String RESPONSE_STATUS_INVALID_REQUEST = "INVALID_REQUEST";
    private static final String HEADER_PARAM_AUTHORIZATION = "authorization";
    private static final String HEADER_PARAM_BEARER = "Bearer";
    private static final String RESPONSE_PARAM_MAKE_LOGIN = "make_login";
    private static final String RESPONSE_PARAM_STATUS = "status";
    private static final String RESPONSE_PARAM_MESSAGE_ERROR = "message_error";

    private Context context;
    private AbstractionRouter abstractionRouter;
    protected UserSession userSession;
    protected String authKey;

    public TkpdAuthInterceptor(@ApplicationContext Context context,
                               AbstractionRouter abstractionRouter,
                               UserSession userSession) {
        this(context, abstractionRouter, userSession, AuthUtil.KEY.KEY_WSV4);
    }

    @Inject
    public TkpdAuthInterceptor(@ApplicationContext Context context,
                               AbstractionRouter abstractionRouter,
                               UserSession userSession,
                               String authKey) {
        this.context = context;
        this.abstractionRouter = abstractionRouter;
        this.userSession = userSession;
        this.authKey = authKey;
    }

    private Lock lock = new ReentrantLock();

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originRequest = chain.request();
        Request.Builder newRequest = chain.request().newBuilder();

        generateHmacAuthRequest(originRequest, newRequest);

        final Request finalRequest = newRequest.build();
        Response response = getResponse(chain, finalRequest);

        if (isNeedRelogin(response)) {
            doRelogin();
            response = getResponse(chain, finalRequest);
        }

        if (!response.isSuccessful()) {
            throwChainProcessCauseHttpError(response);
        }

        if (isUnauthorized(finalRequest, response)) {
            refreshToken();
            Request newest = recreateRequestWithNewAccessToken(chain);
            Response response1 = chain.proceed(newest);
            if (isUnauthorized(newest, response1)) {
                showForceLogoutDialog();
            }
            return response1;
        }

        String bodyResponse = response.body().string();
        checkResponse(bodyResponse, response);

        return createNewResponse(response, bodyResponse);
    }

    private void checkResponse(String string, Response response) {
        String bodyResponse = string;
        if (isMaintenance(bodyResponse)) {
            showMaintenancePage();
        } else if (isRequestDenied(bodyResponse)) {
            showForceLogoutDialog();
        } else if (isServerError(response.code()) && !isHasErrorMessage(bodyResponse)) {
            showServerError(response);
        } else if (isForbiddenRequest(bodyResponse, response.code())
                && isTimezoneNotAutomatic()) {
            showTimezoneErrorSnackbar();
        }
    }


    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        /* this can override for throw error */
    }

    protected boolean isTimezoneNotAutomatic() {
        return MethodChecker.isTimezoneNotAutomatic(context);
    }

    protected boolean isForbiddenRequest(String bodyResponse, int code) {

        JSONObject json;
        try {
            json = new JSONObject(bodyResponse);
            String status = json.optString(RESPONSE_PARAM_STATUS, RESPONSE_STATUS_OK);
            return status.equals(RESPONSE_STATUS_FORBIDDEN) && code == ERROR_FORBIDDEN_REQUEST;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void generateHmacAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        Map<String, String> authHeaders = new HashMap<>();
        authHeaders = prepareHeader(authHeaders, originRequest);
        generateHeader(authHeaders, originRequest, newRequest);
    }

    Map<String, String> prepareHeader(Map<String, String> authHeaders, Request originRequest) {

        String contentTypeHeader = null;
        if (!REQUEST_METHOD_GET.equals(originRequest.method())
                && originRequest.body() != null
                && originRequest.body().contentType() != null)
            contentTypeHeader = originRequest.body().contentType().toString();
        if (REQUEST_METHOD_GET.equalsIgnoreCase(originRequest.method())) contentTypeHeader = "";
        switch (originRequest.method()) {
            case REQUEST_METHOD_PATCH:
            case REQUEST_METHOD_DELETE:
            case REQUEST_METHOD_POST:
                authHeaders = getHeaderMap(
                        originRequest.url().uri().getPath(),
                        generateParamBodyString(originRequest),
                        originRequest.method(),
                        authKey,
                        contentTypeHeader
                );
                break;
            case REQUEST_METHOD_GET:
                authHeaders = getHeaderMap(
                        originRequest.url().uri().getPath(),
                        generateQueryString(originRequest),
                        originRequest.method(),
                        authKey,
                        contentTypeHeader
                );
                break;
        }
        return authHeaders;
    }

    protected Map<String, String> getHeaderMap(
            String path, String strParam, String method, String authKey, String contentTypeHeader) {
        return AuthUtil.generateHeaders(path, strParam, method, authKey, contentTypeHeader, userSession.getUserId());
    }

    void generateHeader(
            Map<String, String> authHeaders, Request originRequest, Request.Builder newRequest
    ) {
        for (Map.Entry<String, String> entry : authHeaders.entrySet())
            newRequest.addHeader(entry.getKey(), entry.getValue());
        newRequest.method(originRequest.method(), originRequest.body());
    }

    String generateParamBodyString(final Request request) {
        try {
            final Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "";
        }
    }

    private String generateQueryString(final Request request) {
        String query = request.url().query();
        return query != null ? query : "";
    }

    protected boolean isMaintenance(String response) {
        JSONObject json;
        try {
            json = new JSONObject(response);
            String status = json.optString(RESPONSE_PARAM_STATUS, RESPONSE_STATUS_OK);
            return status.equals(RESPONSE_STATUS_UNDER_MAINTENANCE);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean isHasErrorMessage(String response) {
        JSONObject json;
        try {
            json = new JSONObject(response);
            JSONArray errorMessage = json.optJSONArray(RESPONSE_PARAM_MESSAGE_ERROR);
            return errorMessage.length() > 0;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean isRequestDenied(String response) {
        JSONObject json;
        try {
            json = new JSONObject(response);
            String status = json.optString(RESPONSE_PARAM_STATUS, RESPONSE_STATUS_OK);
            return status.equals(RESPONSE_STATUS_REQUEST_DENIED);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("unused")
    private boolean isInvalidRequest(String response) {
        JSONObject json;
        try {
            json = new JSONObject(response);
            String status = json.optString(RESPONSE_PARAM_STATUS, RESPONSE_STATUS_OK);
            if (status.equals(RESPONSE_STATUS_INVALID_REQUEST)) return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    protected boolean isServerError(int code) {
        return code >= 500;
    }

    protected Response createNewResponse(Response oldResponse, String oldBodyResponse) {
        ResponseBody body = ResponseBody.create(oldResponse.body().contentType(), oldBodyResponse);

        Response.Builder builder = new Response.Builder();
        builder.body(body)
                .headers(oldResponse.headers())
                .message(oldResponse.message())
                .handshake(oldResponse.handshake())
                .protocol(oldResponse.protocol())
                .cacheResponse(oldResponse.cacheResponse())
                .priorResponse(oldResponse.priorResponse())
                .code(isServerError(oldResponse.code())
                        && isHasErrorMessage(oldBodyResponse) ? 200 : oldResponse.code())
                .request(oldResponse.request())
                .networkResponse(oldResponse.networkResponse());

        return builder.build();
    }

    @Deprecated
    protected void showTimezoneErrorSnackbar() {
        abstractionRouter.showTimezoneErrorSnackbar();
    }

    @Deprecated
    protected void showMaintenancePage() {
        abstractionRouter.showMaintenancePage();
    }

    @Deprecated
    protected void showForceLogoutDialog() {
        abstractionRouter.showForceLogoutDialog();
    }

    @Deprecated
    protected void showServerError(Response response) {
        abstractionRouter.showServerError(response);
    }

    protected Boolean isNeedRelogin(Response response) {
        try {
            //using peekBody instead of body in order to avoid consume response object, peekBody will automatically return new reponse
            String responseString = response.peekBody(512).string();
            return responseString.toUpperCase().contains(RESPONSE_STATUS_REQUEST_DENIED) &&
                    !response.request().url().encodedPath().contains(RESPONSE_PARAM_MAKE_LOGIN);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean isUnauthorized(Request request, Response response) {
        try {
            //using peekBody instead of body in order to avoid consume response object, peekBody will automatically return new reponse
            String responseString = response.peekBody(512).string();
            return responseString.toLowerCase().contains(RESPONSE_STATUS_INVALID_REQUEST)
                    && request.header(HEADER_PARAM_AUTHORIZATION).contains(HEADER_PARAM_BEARER);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void doRelogin() {
        abstractionRouter.refreshLogin();
    }

    protected void refreshToken() {
        abstractionRouter.refreshToken();
    }

    private Request recreateRequestWithNewAccessToken(Chain chain) {
        String freshAccessToken = userSession.getFreshToken();
        return chain.request().newBuilder()
                .header(HEADER_PARAM_AUTHORIZATION, HEADER_PARAM_BEARER + " " + freshAccessToken)
                .build();
    }
}
