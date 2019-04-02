package com.tokopedia.core.network.retrofit.interceptors;

import android.util.Log;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hangnadi on 7/11/17.
 */

@Deprecated
public class ResolutionInterceptor extends TkpdAuthInterceptor {

    private static final String TAG = ResolutionInterceptor.class.getSimpleName();

    public ResolutionInterceptor() {

    }

    @Override
    protected Response getResponse(Chain chain, Request request) throws IOException {
        Response response = chain.proceed(request);
        if (response.code() != 413) {
            Log.d("hangnadi", "getResponse: " + response.code());
            int count = 0;
            while (!response.isSuccessful() && count < maxRetryAttempt) {
                Log.d(TAG, "Request is not successful - " + count + " Error code : " + response.code());
                count++;
                response = chain.proceed(request);
            }
        }
        return response;
    }
}
