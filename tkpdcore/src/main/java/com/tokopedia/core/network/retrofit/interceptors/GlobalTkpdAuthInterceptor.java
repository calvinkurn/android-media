package com.tokopedia.core.network.retrofit.interceptors;

import android.util.Log;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by ricoharisin
 * <p/>
 * use this for API ONLY not WS V4
 * <p/>
 * for WS V4 use TkpdAuthInterceptor
 */
public class GlobalTkpdAuthInterceptor extends TkpdAuthInterceptor {
    private static final String TAG = GlobalTkpdAuthInterceptor.class.getSimpleName();
    private String AuthKey = AuthUtil.KEY.KEY_WSV4;

    public GlobalTkpdAuthInterceptor(String Key) {
        AuthKey = Key;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originRequest = chain.request();
        Request.Builder newRequest = chain.request().newBuilder();

        generateHmacAuthRequest(originRequest, newRequest);

        Request finalRequest = newRequest.build();
        return getResponse(chain, finalRequest);

    }

    @Override
    protected void generateHmacAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        Map<String, String> Headers = AuthUtil.generateHeaders(originRequest.url().uri()
                .getPath(), originRequest.method(), AuthKey);
        for (Map.Entry<String, String> entry : Headers.entrySet()) {
            newRequest.addHeader(entry.getKey(), entry.getValue());
        }

        newRequest.method(originRequest.method(), originRequest.body());
    }


}
