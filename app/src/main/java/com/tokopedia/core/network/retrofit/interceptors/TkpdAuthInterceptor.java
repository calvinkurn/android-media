package com.tokopedia.core.network.retrofit.interceptors;

import android.content.Intent;
import android.util.Log;

import com.tkpd.library.utils.AnalyticsLog;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

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
 * @author Angga.Prasetiyo on 27/11/2015.
 */
public class TkpdAuthInterceptor implements Interceptor {
    private static final String TAG = TkpdAuthInterceptor.class.getSimpleName();
    private final String authKey;

    public TkpdAuthInterceptor(String authKey) {
        this.authKey = authKey;
    }

    public TkpdAuthInterceptor() {
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


        String bodyResponse = response.body().string();
        if (isMaintenance(bodyResponse)) {
            showMaintenancePage();
        } else if (isRequestDenied(bodyResponse)) {
            showForceLogoutDialog();
            sendForceLogoutAnalytics(response);
        } else if (isServerError(response.code())) {
            showServerErrorSnackbar();
            sendErrorNetworkAnalytics(response);
        }


        return createNewResponse(response, bodyResponse);
    }

    protected void generateHmacAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        Map<String, String> authHeaders = new HashMap<>();
        authHeaders = prepareHeader(authHeaders, originRequest);
        generateHeader(authHeaders, originRequest , newRequest);
    }

    Map<String, String> prepareHeader(Map<String, String> authHeaders, Request originRequest) {
        switch (originRequest.method()) {
            case "POST":
                authHeaders = getHeaderMap(originRequest.url().uri().getPath(),
                        generateParamBodyString(originRequest), originRequest.method(), authKey);
                break;
            case "GET":
                authHeaders = getHeaderMap(originRequest.url().uri().getPath(),
                        generateQueryString(originRequest), originRequest.method(), authKey);
                break;
        }
        return authHeaders;
    }

    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey) {
        return AuthUtil.generateHeaders(path, strParam, method, authKey);
    }

    void generateHeader(Map<String, String> authHeaders, Request originRequest, Request.Builder newRequest){
        for (Map.Entry<String, String> entry : authHeaders.entrySet())
            newRequest.addHeader(entry.getKey(), entry.getValue());
        newRequest.method(originRequest.method(), originRequest.body());
    }

    @SuppressWarnings("unused")
    @Deprecated
    /**
     * Move to #generateParamBodyString
     */
    private Map<String, String> generateMapBody(final Request request) {
        try {
            final Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            String bodyStr = buffer.readUtf8();
            Map<String, String> myMap = new HashMap<>();
            String[] pairs = bodyStr.split("&");
            for (String pair : pairs) {
                int indexSplit = pair.indexOf('=');
                String key1 = pair.substring(0, indexSplit);
                String key2 = "";
                if (pair.length() > indexSplit + 1) {
                    key2 = pair.substring(indexSplit + 1);
                }
                myMap.put(key1.trim(), key2.trim());
            }
            return myMap;
        } catch (final IOException e) {
            return new HashMap<>();
        }
    }

    @SuppressWarnings("unused")
    @Deprecated
    /**
     * Move to #generateQueryString
     */
    private Map<String, String> generateMapQuery(final Request request) {
        String bodyStr = request.url().query();
        Map<String, String> myMap = new HashMap<>();
        if (bodyStr != null) {
            String[] pairs = bodyStr.split("&");
            for (String pair : pairs) {
                int indexSplit = pair.indexOf('=');
                String key1 = pair.substring(0, indexSplit);
                String key2 = "";
                if (pair.length() > indexSplit + 1) {
                    key2 = pair.substring(indexSplit + 1);
                }
                myMap.put(key1.trim(), key2.trim());
            }
        }
        return myMap;
    }

    protected String generateParamBodyString(final Request request) {
        try {
            final Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "";
        }
    }

    protected String generateQueryString(final Request request) {
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
                .code(oldResponse.code())
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
