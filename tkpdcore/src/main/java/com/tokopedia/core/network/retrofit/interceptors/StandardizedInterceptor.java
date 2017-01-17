package com.tokopedia.core.network.retrofit.interceptors;

import android.content.Intent;
import android.util.Log;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.exception.SessionExpiredException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import rx.Observable;

/**
 * Created by kris on 1/11/17. Tokopedia
 */

public class StandardizedInterceptor implements Interceptor{

    private static final String TAG = StandardizedInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        try {
            JSONObject jsonResponse = new JSONObject(response.body().string());
            String JSON_ERROR_KEY = "error";
            if(jsonResponse.has(JSON_ERROR_KEY)) {
                handleError(jsonResponse.getString(JSON_ERROR_KEY));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int count = 0;
        while (!response.isSuccessful() && count < 3) {
            Log.d(TAG, "Request is not successful - " + count + " Error code : " + response.code());
            count++;
            response = chain.proceed(chain.request());
        }
        return response;
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

}
