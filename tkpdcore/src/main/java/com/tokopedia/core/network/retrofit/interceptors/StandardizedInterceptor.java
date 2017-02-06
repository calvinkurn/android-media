package com.tokopedia.core.network.retrofit.interceptors;

import android.content.Intent;
import android.util.Log;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.exception.SessionExpiredException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by kris on 1/11/17. Tokopedia
 */

public class StandardizedInterceptor implements Interceptor{

    private static final String TAG = StandardizedInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();

        final Request finalRequest = newRequest.build();
        Response response = chain.proceed(finalRequest);
        int count = 0;
        while (!response.isSuccessful() && count < 3) {
            Log.d(TAG, "Request is not successful - " + count + " Error code : " + response.code());
            count++;
            response = chain.proceed(chain.request());
        }

        String bodyResponse = response.body().string();
        try {
            JSONObject jsonResponse = new JSONObject(bodyResponse);
            String JSON_ERROR_KEY = "error";
            if(jsonResponse.has(JSON_ERROR_KEY)) {
                handleError(jsonResponse.getString(JSON_ERROR_KEY));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createNewResponse(response, bodyResponse);
    }

    private void handleError(String errorMessage) throws SessionExpiredException{
        if(errorMessage.equals("invalid_request") || errorMessage.equals("invalid_grant"))
            throw new SessionExpiredException(errorMessage);
        /*if(errorMessage.equals("invalid_request"))
            throw new SessionExpiredExce
            ption("invalid_request");
        else if(errorMessage.equals("invalid_grant"))
            throw new SessionExpiredException("invalid_grant");*/
    }

    private void showForceLogoutDialog() {
        Intent intent = new Intent();
        intent.setAction("com.tokopedia.tkpd.FORCE_LOGOUT");
        MainApplication.getAppContext().sendBroadcast(intent);
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

}
