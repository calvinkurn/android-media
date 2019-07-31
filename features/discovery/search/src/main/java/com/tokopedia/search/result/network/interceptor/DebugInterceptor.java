package com.tokopedia.search.result.network.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class DebugInterceptor implements Interceptor {

    DebugInterceptor() {

    }

    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();
        addDebugHeader(newRequest);

        return chain.proceed(newRequest.build());
    }

    private void addDebugHeader(Request.Builder newRequest) {
        newRequest.addHeader("BuildType", "DEBUG");
    }
}