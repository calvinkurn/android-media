package com.tokopedia.core.network.retrofit.interceptors;

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
        throw new HttpErrorException(response.code());
    }
}
