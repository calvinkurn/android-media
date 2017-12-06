package com.tokopedia.core.network.retrofit.interceptors;

import android.util.Log;

import com.tokopedia.core.network.exception.HttpErrorException;

import java.io.IOException;

import okhttp3.Response;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class TokoPointAuthInterceptor extends TkpdAuthInterceptor {
    private static final String TAG = TokoPointAuthInterceptor.class.getSimpleName();

    public TokoPointAuthInterceptor(String hmacKey) {
        super(hmacKey);
    }

    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        if (response.body() != null)
            Log.d("HCK RESPONSE ERROR: ", response.body().string() == null ? "" : response.body().string());
        response.body().close();
        throw new HttpErrorException(response.code());
    }
}
