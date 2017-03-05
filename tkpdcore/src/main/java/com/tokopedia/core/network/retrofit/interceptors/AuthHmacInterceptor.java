package com.tokopedia.core.network.retrofit.interceptors;

import android.content.Intent;
import android.util.Log;

import com.tkpd.library.utils.AnalyticsLog;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * @author anggaprasetiyo on 3/4/17.
 */

abstract class AuthHmacInterceptor implements Interceptor {

    private static final String TAG = AuthHmacInterceptor.class.getSimpleName();
    private final String authKey;

    AuthHmacInterceptor(String authKey) {
        this.authKey = authKey;
    }

    public AuthHmacInterceptor() {
        this.authKey = AuthUtil.KEY.KEY_WSV4;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originRequest = chain.request();
        Request.Builder newRequest = chain.request().newBuilder();

        generateHmacAuthRequest(originRequest, newRequest);

        final Request finalRequest = newRequest.build();
        Response response = chain.proceed(finalRequest);
        int count = 0;
        while (!response.isSuccessful() && count < 3) {
            Log.d(TAG, "Request is not successful - " + count + " Error code : " + response.code());
            count++;
            response = chain.proceed(finalRequest);
        }
        if (!response.isSuccessful()) {
            throwChainProcessCauseHttpError(response.code());
        }

        String bodyResponse = response.body().string();
        if (isMaintenance(bodyResponse)) {
            showMaintenancePage();
        } else if (isRequestDenied(bodyResponse)) {
            showForceLogoutDialog();
            sendForceLogoutAnalytics(response);
        } else if (isServerError(response.code()) && !isHasErrorMessage(bodyResponse)) {
            showServerErrorSnackbar();
            sendErrorNetworkAnalytics(response);
        }
        return createNewResponse(response, bodyResponse);
    }

    protected abstract void throwChainProcessCauseHttpError(int code) throws IOException;

    private void generateHmacAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        Map<String, String> authHeaders = new HashMap<>();
        authHeaders = prepareHeader(authHeaders, originRequest);
        generateHeader(authHeaders, originRequest, newRequest);
    }

    private Map<String, String> prepareHeader(
            Map<String, String> authHeaders, Request originRequest
    ) {
        String contentTypeHeader = originRequest.header("Content-Type");
        switch (originRequest.method()) {
            case "PATCH":
            case "DELETE":
            case "POST":
                authHeaders = getHeaderMap(
                        originRequest.url().uri().getPath(),
                        generateParamBodyString(originRequest), originRequest.method(),
                        authKey, contentTypeHeader
                );
                break;
            case "GET":
                authHeaders = getHeaderMap(
                        originRequest.url().uri().getPath(),
                        generateQueryString(originRequest),
                        originRequest.method(), authKey, contentTypeHeader
                );
                break;
        }
        return authHeaders;
    }

    private Map<String, String> getHeaderMap(
            String path, String strParam, String method, String authKey, String contentTypeHeader
    ) {
        return AuthUtil.generateHeaders(path, strParam, method, authKey, contentTypeHeader);
    }

    private void generateHeader(
            Map<String, String> authHeaders, Request originRequest, Request.Builder newRequest
    ) {
        for (Map.Entry<String, String> entry : authHeaders.entrySet())
            newRequest.addHeader(entry.getKey(), entry.getValue());
        newRequest.method(originRequest.method(), originRequest.body());
    }

    private String generateParamBodyString(final Request request) {
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

    private Boolean isMaintenance(String response) {
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

    private boolean isHasErrorMessage(String response) {
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

    private Boolean isRequestDenied(String response) {
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

    private Boolean isInvalidRequest(String response) {
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

    private Boolean isServerError(int code) {
        return code >= 500;
    }

    private Response createNewResponse(Response oldResponse, String oldBodyResponse) {
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

    private void showMaintenancePage() {
        MainApplication.getAppContext().startActivity(
                MaintenancePage.createIntentFromNetwork(MainApplication.getAppContext()));
    }

    private void showForceLogoutDialog() {
        Intent intent = new Intent();
        intent.setAction("com.tokopedia.tkpd.FORCE_LOGOUT");
        MainApplication.getAppContext().sendBroadcast(intent);
    }

    private void sendForceLogoutAnalytics(Response response) {
        AnalyticsLog.logForceLogout(response.request().url().toString());
    }

    private void showServerErrorSnackbar() {
        Intent intent = new Intent();
        intent.setAction("com.tokopedia.tkpd.SERVER_ERROR");
        MainApplication.getAppContext().sendBroadcast(intent);
    }

    private void sendErrorNetworkAnalytics(Response response) {
        AnalyticsLog.logNetworkError(response.request().url().toString(), response.code());
    }
}
