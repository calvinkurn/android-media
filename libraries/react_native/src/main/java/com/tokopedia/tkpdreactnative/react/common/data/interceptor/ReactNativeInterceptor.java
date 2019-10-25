package com.tokopedia.tkpdreactnative.react.common.data.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by alvarisi on 10/9/17.
 */

public class ReactNativeInterceptor implements Interceptor {
    protected HashMap<String, String> headers;

    public ReactNativeInterceptor(HashMap<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        return proceedRequest(chain, buildNewRequest(chain.request(), builder));
    }

    private Response proceedRequest(Chain chain, Request request) throws IOException {
        return chain.proceed(request);
    }

    protected Request buildNewRequest(Request request, Request.Builder builder) {
        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.addHeader(header.getKey(), String.valueOf(header.getValue()));
        }
        builder.method(request.method(), request.body());
        return builder.build();
    }
}
