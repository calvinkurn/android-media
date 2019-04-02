package com.tokopedia.core.network.retrofit.interceptors;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kris on 1/19/17. Tokopedia
 */

@Deprecated
public class KeroInterceptor implements Interceptor{
    private String AuthKey = AuthUtil.KEY.KEY_WSV4;

    public KeroInterceptor(String authKey) {
        AuthKey = authKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originRequest = chain.request();
        Request.Builder newRequest = chain.request().newBuilder();
        Map<String, String> Headers = AuthUtil.generateHeaders(originRequest.url().uri()
                .getPath(), originRequest.method(), AuthKey);
        for (Map.Entry<String, String> entry : Headers.entrySet()) {
            newRequest.addHeader(entry.getKey(), entry.getValue());
        }
        newRequest.method(originRequest.method(), originRequest.body());
        final Request finalRequest = newRequest.build();
        Response response = chain.proceed(finalRequest);
        return response;
    }
}
