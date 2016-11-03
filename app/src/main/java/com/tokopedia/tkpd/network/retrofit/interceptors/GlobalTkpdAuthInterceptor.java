package com.tokopedia.tkpd.network.retrofit.interceptors;

import android.util.Log;

import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;

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
public class GlobalTkpdAuthInterceptor implements Interceptor {
    private static final String TAG = GlobalTkpdAuthInterceptor.class.getSimpleName();
    private String AuthKey = AuthUtil.KEY.KEY_WSV4;

    public GlobalTkpdAuthInterceptor(String Key) {
        AuthKey = Key;
    }

    public GlobalTkpdAuthInterceptor() {
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
        Log.d(TAG, "MAP HEADER = " + Headers.toString());
        Log.d(TAG, "METHOD = " + originRequest.method());
        Log.d(TAG, "URI = " + originRequest.url().uri().toString());
        Log.d(TAG, "URI PATH = " + originRequest.url().uri().getPath());
        final Request finalRequest = newRequest.build();
        Response response = chain.proceed(finalRequest);
        int count = 0;
        while (!response.isSuccessful() && count < 3) {
            Log.d(TAG, "Request is not successful - " + count + " Error code : " + response.code());
            count++;
            response = chain.proceed(finalRequest);
        }
        return response;
    }

    private Map<String, String> generateMapBody(final Request request) {
        try {
            final Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            String bodyStr = buffer.readUtf8();
            Map<String, String> myMap = new HashMap<>();
            String[] pairs = bodyStr.split("&");
            for (String pair : pairs) {
                int indexSplit = pair.indexOf('=');
                String key1 = pair.substring(0, indexSplit);
                String key2 = "";
                if (pair.length() > indexSplit + 1) {
                    key2 = pair.substring(indexSplit + 1);
                }
                myMap.put(key1.trim(), key2.trim());
            }
            return myMap;
        } catch (final IOException e) {
            return new HashMap<>();
        }
    }
}
