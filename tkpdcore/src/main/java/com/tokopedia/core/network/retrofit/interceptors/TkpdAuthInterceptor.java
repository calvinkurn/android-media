package com.tokopedia.core.network.retrofit.interceptors;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.util.AccessTokenRefresh;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.SessionRefresh;

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
    private static final String BEARER = "Bearer";
    private static final String AUTHORIZATION = "authorization";
    private static final String TOKEN = "token";
    private static final String ACCOUNTS_AUTHORIZATION = "accounts-authorization";
    private final String authKey;

    public TkpdAuthInterceptor(String authKey) {
        this.authKey = authKey;
    }

    public TkpdAuthInterceptor() {
        this.authKey = AuthUtil.KEY.KEY_WSV4;
    }

    private Lock lock = new ReentrantLock();

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originRequest = chain.request();
        Request.Builder newRequest = chain.request().newBuilder();

        generateHmacAuthRequest(originRequest, newRequest);

        final Request finalRequest = newRequest.build();
        Response response = getResponse(chain, finalRequest);

        if (!response.isSuccessful()) {
            throwChainProcessCauseHttpError(response);
        }

        checkForceLogout(chain, response, finalRequest);

        String bodyResponse = response.body().string();
        checkResponse(bodyResponse, response);

        return createNewResponse(response, bodyResponse);
    }

    protected Response checkForceLogout(Chain chain, Response response, Request finalRequest) throws
            IOException {
        if (isNeedRelogin(response)) {
            refreshTokenWithRelogin();
            if (finalRequest.header(AUTHORIZATION).contains(BEARER)) {
                Request newestRequest = recreateRequestWithNewAccessToken(chain);
                return checkShowForceLogout(chain, newestRequest);
            } else {
                Request newestRequest = recreateRequestWithNewAccessTokenAccountsAuth(chain);
                return checkShowForceLogout(chain, newestRequest);
            }
        } else if (isUnauthorized(finalRequest, response)) {
            refreshToken();
            Request newest = recreateRequestWithNewAccessToken(chain);
            return checkShowForceLogout(chain, newest);
        }
        return response;
    }

    protected Response checkShowForceLogout(Chain chain, Request newestRequest) throws IOException {
        Response response = chain.proceed(newestRequest);
        if (isUnauthorized(newestRequest, response) || isNeedRelogin(response)) {
            ServerErrorHandler.showForceLogoutDialog();
            ServerErrorHandler.sendForceLogoutAnalytics(response.request().url().toString());
        }
        return response;
    }

    protected void checkResponse(String string, Response response) {
        String bodyResponse = string;

        if (isOnBetaServer(response)) ServerErrorHandler.showForceHockeyAppDialog();

        if (isMaintenance(bodyResponse)) {
            ServerErrorHandler.showMaintenancePage();
        } else if (isServerError(response.code()) && !isHasErrorMessage(bodyResponse)) {
            ServerErrorHandler.showServerErrorSnackbar();
            ServerErrorHandler.sendErrorNetworkAnalytics(response.request().url().toString(),
                    response.code());
        } else if (isForbiddenRequest(bodyResponse, response.code())
                && isTimezoneNotAutomatic()) {
            ServerErrorHandler.showTimezoneErrorSnackbar();
        }
    }


    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        /* this can override for throw error */
    }


    protected boolean isTimezoneNotAutomatic() {
        return MethodChecker.isTimezoneNotAutomatic();
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

    protected Map<String, String> prepareHeader(Map<String, String> authHeaders, Request originRequest) {

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
            case "PUT":
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
        return AuthUtil.generateHeaders(path, strParam, method, authKey, contentTypeHeader);
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
            if (errorMessage != null)
                return errorMessage.length() > 0;
            else
                return false;
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

    protected Boolean isNeedRelogin(Response response) {
        try {
            //using peekBody instead of body in order to avoid consume response object, peekBody will automatically return new reponse
            String responseString = response.peekBody(512).string();
            return responseString.toUpperCase().contains("REQUEST_DENIED") &&
                    !response.request().url().encodedPath().contains("make_login");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean isUnauthorized(Request request, Response response) {
        try {
            //using peekBody instead of body in order to avoid consume response object, peekBody will automatically return new reponse
            String responseString = response.peekBody(512).string();
            return responseString.toLowerCase().contains("invalid_request")
                    && request.header(AUTHORIZATION).contains(BEARER)
                    && !response.request().url().encodedPath().contains(TOKEN)
                    && !response.request().url().encodedPath().contains("token");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void doRelogin() {
        doRelogin("");
    }

    protected void doRelogin(String newAccessToken) {
        SessionRefresh sessionRefresh = new SessionRefresh(newAccessToken);
        try {
            sessionRefresh.refreshLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void refreshToken() {
        AccessTokenRefresh accessTokenRefresh = new AccessTokenRefresh();
        try {
            accessTokenRefresh.refreshToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void refreshTokenWithRelogin() {
        AccessTokenRefresh accessTokenRefresh = new AccessTokenRefresh();
        try {
            String newAccessToken = accessTokenRefresh.refreshToken();
            doRelogin(newAccessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }private Request recreateRequestWithNewAccessToken(Chain chain) throws IOException{
        Request newest = chain.request();
        Request.Builder newestRequestBuilder = chain.request().newBuilder();
        generateHmacAuthRequest(newest, newestRequestBuilder);
        String freshAccessToken = SessionHandler.getAccessToken();
        newestRequestBuilder
                .header(AUTHORIZATION, BEARER + " " + freshAccessToken)
                .header(ACCOUNTS_AUTHORIZATION, BEARER + " " + freshAccessToken);
        return newestRequestBuilder.build();
    }

    private Request recreateRequestWithNewAccessTokenAccountsAuth(Chain chain) throws IOException {
        Request newest = chain.request();
        Request.Builder newestRequestBuilder = chain.request().newBuilder();
        generateHmacAuthRequest(newest, newestRequestBuilder);
        String freshAccessToken = SessionHandler.getAccessToken();
        newestRequestBuilder
                .header(ACCOUNTS_AUTHORIZATION, BEARER + " " + freshAccessToken);
        return newestRequestBuilder.build();
    }

    private Boolean isOnBetaServer(Response response) {
        return response.header("is_beta", "0").equals("1");
    }

}
