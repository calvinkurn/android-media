package com.tokopedia.abstraction.common.network.interceptor;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Angga.Prasetiyo on 23/12/2015.
 */
public class TkpdBaseInterceptor implements Interceptor {
    private static final String TAG = TkpdBaseInterceptor.class.getSimpleName();
    protected int maxRetryAttempt = 3;

    @Inject
    public TkpdBaseInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return getResponse(chain, chain.request());
    }

    protected Response getResponse(Chain chain, Request request) throws IOException {
        try {
            Response response = chain.proceed(request);
            int count = 0;
            while (!response.isSuccessful() && count < maxRetryAttempt) {
                count++;
                response = chain.proceed(request);
            }
            return response;
        } catch (Error e) {
            throw new UnknownHostException("tidak ada koneksi internet");
        }
    }

    public int getMaxRetryAttempt() {
        return maxRetryAttempt;
    }

    public TkpdBaseInterceptor setMaxRetryAttempt(int maxRetryAttempt) {
        this.maxRetryAttempt = maxRetryAttempt;
        return this;
    }
}
