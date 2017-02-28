package com.tokopedia.core.network.core;

import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBaseInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ricoharisin on 2/27/17.
 */

public class OkHttpFactory {

    private final OkHttpClient client = getDefaultClient();

    private OkHttpClient getDefaultClient() {
        return getDefaultClientConfig(new OkHttpClient.Builder())
                .build();
    }

    private OkHttpClient.Builder getDefaultClientConfig(OkHttpClient.Builder builder) {
        return builder
                .addInterceptor(getHttpLoggingInterceptor())
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS);
    }

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public OkHttpClient createClientAuth(String authKey) {
        return getDefaultClientConfig(client.newBuilder())
                .addInterceptor(new TkpdAuthInterceptor(authKey))
                .build();
    }

    public OkHttpClient createClientDefaultAuth() {
        return getDefaultClientConfig(client.newBuilder())
                .addInterceptor(new TkpdAuthInterceptor())
                .build();
    }

    public OkHttpClient createClientNoAuth() {
        return getDefaultClientConfig(client.newBuilder())
                .addInterceptor(new TkpdBaseInterceptor())
                .build();
    }

    public OkHttpClient createClientKero() {
        return getDefaultClientConfig(client.newBuilder())
                .addInterceptor(new TkpdAuthInterceptor().setMaxRetryAttempt(0))
                .build();

    }

}
