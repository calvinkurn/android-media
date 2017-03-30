package com.tokopedia.core.network.retrofit.interceptors;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ricoharisin on 3/20/17.
 */

public class DebugInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();
        newRequest = addDebugHeader(newRequest);

        return chain.proceed(newRequest.build());
    }

    private Request.Builder addDebugHeader(Request.Builder newRequest) {
        newRequest.addHeader("BuildType", "DEBUG");
        return newRequest;
    }
}
