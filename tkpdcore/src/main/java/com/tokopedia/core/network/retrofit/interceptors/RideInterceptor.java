package com.tokopedia.core.network.retrofit.interceptors;

import android.content.Intent;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.exception.SessionExpiredException;
import com.tokopedia.core.network.exception.InterruptConfirmationHttpException;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by alvarisi on 3/14/17.
 */

public class RideInterceptor extends TkpdAuthInterceptor {
    private static final String TAG = RideInterceptor.class.getSimpleName();
    private static final String HEADER_DATE_FORMAT = "dd MMM yy HH:mm ZZZ";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String HEADER_DATE = "X-Date";
    private static final String HEADER_DEVICE = "X-Device";
    private static final String HEADER_USER_ID = "Tkpd-UserId";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_X_AUTHORIZATION = "X-Tkpd-Authorization";
    private static final String AUTO_RIDE = "AUTO_RIDE";
    private static final String DEFAULT_ERROR_MESSAGE_DATA_NULL = "Tidak ada data";
    private String authorizationString;

    public RideInterceptor(String authorizationString, String userId) {
        this.authorizationString = authorizationString;
        this.maxRetryAttempt = 0;
    }

    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        String bodyResponse = response.body().string();
        int code = response.code();
        switch (code) {
            //case 404:
            //case 422:
            //case 500:
            //response.body().close();
            //throw new UnProcessableHttpException(bodyResponse);
            case 409:
                response.body().close();
                throw new InterruptConfirmationHttpException(bodyResponse);
            default:
                try {
                    response.body().close();
                    JSONObject jsonResponse = new JSONObject(bodyResponse);
                    String JSON_ERROR_KEY = "message_error";
                    if (jsonResponse.has(JSON_ERROR_KEY)) {

                        JSONArray messageErrorArray = jsonResponse.optJSONArray(JSON_ERROR_KEY);
                        if (messageErrorArray != null) {
                            String message = "";
                            for (int index = 0; index < messageErrorArray.length(); index++) {
                                if (index > 0) {
                                    message += ", ";
                                }
                                message = message + messageErrorArray.getString(index);
                            }
                            handleError(bodyResponse, message);

                        } else {
                            handleError(bodyResponse, jsonResponse.getString(JSON_ERROR_KEY));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    private void handleError(String bodyResponse, String errorMessage) throws IOException {
        if (errorMessage.equals("invalid_request") || errorMessage.equals("invalid_grant")) {
            Intent intent = new Intent();
            intent.setAction(BaseActivity.FORCE_LOGOUT);
            MainApplication.getAppContext().sendBroadcast(intent);
            throw new SessionExpiredException(errorMessage);
        } else {
            throw new UnProcessableHttpException(bodyResponse);
        }
    }

    protected Response createNewResponse(Response response, String responseString) {
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            if (jsonObject.has("data")) {
                String newResponseString = jsonObject.getString("data");
                return constructNewResponse(response, newResponseString);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return response;
        }
        return response;
    }

    protected Response constructNewResponse(Response oldResponse, String oldBodyResponse) {
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

    /*private Request.Builder getBearerHeaderBuilder(Request request, String oAuth, String userId) {

        return request.newBuilder()
                .header("Tkpd-UserId", userId)
                .header("Authorization", oAuth)
                .header("X-Device", "android-" + GlobalConfig.VERSION_NAME)
                .header("Content-Type", "application/x-www-form-urlencoded")
                //TODO remove skip payment and auto ride
                //.header("tkpd-skip-payment", "true")
                .header("", "true")
                .header(HEADER_X_APP_VERSION, "android-" + String.valueOf(GlobalConfig.VERSION_NAME))
                .method(request.method(), request.body());
    }*/

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        Map<String, String> headerMap = AuthUtil.getDefaultHeaderMap(path, strParam, method, CONTENT_TYPE, authKey, HEADER_DATE_FORMAT);
        SimpleDateFormat dateFormat = new SimpleDateFormat(HEADER_DATE_FORMAT, Locale.ENGLISH);
        String date = dateFormat.format(new Date());
        headerMap.put(HEADER_DATE, date);
        headerMap.put(HEADER_USER_ID, SessionHandler.getLoginID(MainApplication.getAppContext()));
        headerMap.put(HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);

        headerMap.put(HEADER_X_AUTHORIZATION, headerMap.get(HEADER_AUTHORIZATION));
        headerMap.put(HEADER_AUTHORIZATION, authorizationString);
        headerMap.put(AUTO_RIDE, "true");
        return headerMap;
    }

    protected Response getResponse(Chain chain, Request request) throws IOException {
        try {
            return chain.proceed(request);
        } catch (Error e) {
            throw new UnknownHostException("tidak ada koneksi internet");
        }
    }
}
