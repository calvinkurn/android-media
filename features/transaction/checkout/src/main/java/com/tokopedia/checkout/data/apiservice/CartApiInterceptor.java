package com.tokopedia.checkout.data.apiservice;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Response;

/**
 * @author anggaprasetiyo on 23/04/18.
 */
public class CartApiInterceptor extends TkpdAuthInterceptor {

    @Inject
    public CartApiInterceptor(Context context, AbstractionRouter abstractionRouter,
                              UserSession userSession, String authKey) {
        super(context, abstractionRouter, userSession, authKey);
    }

    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        String responseError = response.body().string();
        Log.d("CARTAPI ERROR = ", responseError);
        if (responseError != null && !responseError.isEmpty() && responseError.contains("header")) {
            CartErrorResponse cartErrorResponse = new Gson().fromJson(
                    responseError, CartErrorResponse.class
            );
            if (cartErrorResponse.getCartHeaderResponse() != null) {
                throw new CartResponseErrorException(
                        response.code(),
                        cartErrorResponse.getCartHeaderResponse());
            }
        }
        throw new HttpErrorException(response.code());
    }

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method,
                                               String authKey, String contentTypeHeader) {
        Log.d("CARTAPI PATH = ", path);
        Log.d("CARTAPI PARAM QUERY = ", strParam);
        Log.d("CARTAPI METHOD = ", method);
        Log.d("CARTAPI AUTH KEY = ", authKey);
        Log.d("CARTAPI CONTENT TYPE = ", contentTypeHeader);
        Map<String, String> mapHeader = AuthUtil.getDefaultHeaderMap(
                path,
                strParam,
                method,
                contentTypeHeader != null ? contentTypeHeader : "application/x-www-form-urlencoded",
                authKey,
                "EEE, dd MMM yyyy HH:mm:ss ZZZ", userSession.getUserId());

        mapHeader.put("X-APP-VERSION", GlobalConfig.VERSION_NAME);
        mapHeader.put("Tkpd-UserId", userSession.getUserId());
        mapHeader.put("X-Device", "android");
        mapHeader.put("Tkpd-SessionId", userSession.getDeviceId());

        for (Map.Entry<String, String> entry : mapHeader.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d("CARTAPI HEADER = ", "KEY = " + key + "| VALUE = " + value);
        }

        return mapHeader;
    }
}
