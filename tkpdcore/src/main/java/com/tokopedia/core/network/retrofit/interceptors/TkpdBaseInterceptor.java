package com.tokopedia.core.network.retrofit.interceptors;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Angga.Prasetiyo on 23/12/2015.
 */
public class TkpdBaseInterceptor implements Interceptor {
    private static final String TAG = TkpdBaseInterceptor.class.getSimpleName();
    protected int maxRetryAttempt = 3;

    public TkpdBaseInterceptor() {}

    @Override
    public Response intercept(Chain chain) throws IOException {
        return getResponse(chain, chain.request());
    }

    protected Response getResponse(Chain chain, Request request) throws IOException {
        Response response = chain.proceed(request);
        int count = 0;
        while (!response.isSuccessful() && count < maxRetryAttempt) {
            Log.d(TAG, "Request is not successful - " + count + " Error code : " + response.code());
            count++;
            response = chain.proceed(request);
        }
        return response;
    }

    public int getMaxRetryAttempt() {
        return maxRetryAttempt;
    }

    public TkpdBaseInterceptor setMaxRetryAttempt(int maxRetryAttempt) {
        this.maxRetryAttempt = maxRetryAttempt;
        return this;
    }
}
