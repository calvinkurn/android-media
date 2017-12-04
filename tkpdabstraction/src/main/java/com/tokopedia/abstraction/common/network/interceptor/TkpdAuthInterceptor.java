package com.tokopedia.abstraction.common.network.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.utils.AuthUtil;
import com.tokopedia.abstraction.utils.MethodChecker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * @author Angga.Prasetiyo on 27/11/2015.
 */
public class TkpdAuthInterceptor extends TkpdBaseInterceptor {
    private static final String TAG = TkpdAuthInterceptor.class.getSimpleName();
    private static final int ERROR_FORBIDDEN_REQUEST = 403;
    private static final String ACTION_TIMEZONE_ERROR = "com.tokopedia.tkpd.TIMEZONE_ERROR";
    private final String authKey;
    private Context context;
    private String freshAccessToken;
    private AbstractionRouter abstractionRouter;
    protected UserSession userSession;

    public TkpdAuthInterceptor(String authKey,
                               Context context,
                               String freshAccessToken,
                               AbstractionRouter abstractionRouter,
                               UserSession userSession) {
        this.authKey = authKey;
        this.context = context;
        this.freshAccessToken = freshAccessToken;
        this.abstractionRouter = abstractionRouter;
        this.userSession = userSession;
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
                sendForceLogoutAnalytics(response1);
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
            sendForceLogoutAnalytics(response);
        } else if (isServerError(response.code()) && !isHasErrorMessage(bodyResponse)) {
            showServerErrorSnackbar();
            sendErrorNetworkAnalytics(response);
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
            String status = json.optString("status", "OK");
            return status.equals("FORBIDDEN") && code == ERROR_FORBIDDEN_REQUEST;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void generateHmacAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        Map<String, String> authHeaders = new HashMap<>();
        authHeaders = prepareHeader(authHeaders, originRequest);
        //   Log.d(TAG, "header: " + new PrettyPrintingMap<>(authHeaders).toString());
        generateHeader(authHeaders, originRequest, newRequest);
    }

    Map<String, String> prepareHeader(Map<String, String> authHeaders, Request originRequest) {

        String contentTypeHeader = null;
        if (!"GET".equals(originRequest.method())
                && originRequest.body() != null
                && originRequest.body().contentType() != null)
            contentTypeHeader = originRequest.body().contentType().toString();
        if ("GET".equalsIgnoreCase(originRequest.method())) contentTypeHeader = "";
        switch (originRequest.method()) {
            case "PATCH":
            case "DELETE":
            case "POST":
                authHeaders = getHeaderMap(
                        originRequest.url().uri().getPath(),
                        generateParamBodyString(originRequest),
                        originRequest.method(),
                        authKey,
                        contentTypeHeader
                );
                break;
            case "GET":
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
            String status = json.optString("status", "OK");
            return status.equals("UNDER_MAINTENANCE");
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean isHasErrorMessage(String response) {
        JSONObject json;
        try {
            json = new JSONObject(response);
            JSONArray errorMessage = json.optJSONArray("message_error");
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
            String status = json.optString("status", "OK");
            return status.equals("REQUEST_DENIED");
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
            String status = json.optString("status", "OK");
            if (status.equals("INVALID_REQUEST")) return true;
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
    protected void sendForceLogoutAnalytics(Response response) {
        abstractionRouter.sendForceLogoutAnalytics(response.request().url().toString());
    }

    @Deprecated
    protected void showServerErrorSnackbar() {
        abstractionRouter.showServerErrorSnackbar();
    }

    @Deprecated
    protected void sendErrorNetworkAnalytics(Response response) {
        abstractionRouter.sendErrorNetworkAnalytics(response.request().url().toString(), response.code());
    }

    protected Boolean isNeedRelogin(Response response) {
        try {
            //using peekBody instead of body in order to avoid consume response object, peekBody will automatically return new reponse
            String responseString = response.peekBody(512).string();
            return responseString.toUpperCase().contains("REQUEST_DENIED") &&
                    !response.request().url().encodedPath().contains("make_login");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean isUnauthorized(Request request, Response response) {
        try {
            //using peekBody instead of body in order to avoid consume response object, peekBody will automatically return new reponse
            String responseString = response.peekBody(512).string();
            return responseString.toLowerCase().contains("invalid_request")
                    && request.header("authorization").contains("Bearer");
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
        String freshAccessToken = this.freshAccessToken;
        return chain.request().newBuilder()
                .header("authorization", "Bearer " + freshAccessToken)
                .build();
    }
}
