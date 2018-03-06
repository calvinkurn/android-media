package com.tokopedia.core.network.retrofit.interceptors;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.exception.SessionExpiredException;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by ashwanityagi on 15/11/17.
 */

public class EventInerceptors extends TkpdAuthInterceptor {
    private static final String TAG = EventInerceptors.class.getSimpleName();
    private static final String HEADER_DATE_FORMAT = "dd MMM yy HH:mm ZZZ";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String HEADER_DATE = "X-Date";
    private static final String HEADER_DEVICE = "X-Device";
    private static final String HEADER_USER_ID = "Tkpd-UserId";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_X_AUTHORIZATION = "X-Tkpd-Authorization";
    private static final String DEFAULT_ERROR_MESSAGE_DATA_NULL = "Tidak ada data";
    private String authorizationString;
    private Context mContext;

    public EventInerceptors(String oAuth, Context context) {
        this.authorizationString = oAuth;
        mContext = context;

    }


    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        String bodyResponse = response.body().string();
        int code = response.code();
        switch (code) {
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
                    throw new UnProcessableHttpException();
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


    protected Response getResponse(Chain chain, Request request) throws IOException {
        try {
            return chain.proceed(request);
        } catch (Error e) {
            throw new UnknownHostException("tidak ada koneksi internet");
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String chainURL = chain.request().url().url().toString();
        if (chainURL.contains(TkpdBaseURL.EVENTS_DOMAIN + "v1/api/expresscart/verify?")
                || chainURL.contains(TkpdBaseURL.EVENTS_DOMAIN + "v1/api/expresscart/checkout")) {
            final Request originRequest = chain.request();
            Request.Builder newRequest = chain.request().newBuilder();

            generateHmacAuthRequest(originRequest, newRequest);
            newRequest.removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer " + SessionHandler.getAccessToken())
                    .addHeader("Tkpd-UserId", SessionHandler.getLoginID(mContext));

            final Request finalRequest = newRequest.build();
            Response response = getResponse(chain, finalRequest);

            if (isNeedRelogin(response)) {
                doRelogin();
                response = getResponse(chain, finalRequest);
            }

            if (!response.isSuccessful()) {
                throwChainProcessCauseHttpError(response);
            }

            String bodyResponse = response.body().string();
            checkResponse(bodyResponse, response);

            return createNewResponse(response, bodyResponse);
        } else
            return super.intercept(chain);
    }
}
