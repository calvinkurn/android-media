package com.tokopedia.core.network.retrofit.interceptors;

import android.content.Intent;

import com.tokopedia.core.app.MainApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.Response;

/**
 * Created by kris on 1/11/17. Tokopedia
 */

public class StandardizedInterceptor extends TkpdBaseInterceptor{

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

        return super.intercept(chain);
    }

    private void handleError(String errorMessage) {
        if(errorMessage.equals("invalid_request")) showForceLogoutDialog();
        else if(errorMessage.equals("invalid_grant")) showForceLogoutDialog();
    }

    private void showForceLogoutDialog() {
        Intent intent = new Intent();
        intent.setAction("com.tokopedia.tkpd.FORCE_LOGOUT");
        MainApplication.getAppContext().sendBroadcast(intent);
    }

}
