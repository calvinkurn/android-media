package com.tokopedia.core.network.retrofit.interceptors;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author Angga.Prasetiyo on 23/12/2015.
 */
public class TkpdBaseInterceptor implements Interceptor {
    private static final String TAG = TkpdBaseInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        int count = 0;
        while (!response.isSuccessful() && count < 3) {
            Log.d(TAG, "Request is not successful - " + count + " Error code : " + response.code());
            count++;
            response = chain.proceed(chain.request());
        }
        return response;
    }
}
