package com.tokopedia.network.utils;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.network.interceptor.TkpdBaseInterceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ricoharisin on 2/28/17.
 */

public class TkpdOkHttpBuilder {

    private OkHttpClient.Builder builder;
    private Context context;
    private int MAX_REQUEST_PER_HOST = 10;

    public TkpdOkHttpBuilder(Context context, OkHttpClient.Builder builder) {
        this.builder = builder;
        this.context = context;
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(MAX_REQUEST_PER_HOST);
        this.builder.dispatcher(dispatcher);
    }

    public OkHttpClient.Builder getBuilder() {
        return builder;
    }

    public TkpdOkHttpBuilder addInterceptor(Interceptor interceptor) {
        builder.addInterceptor(interceptor);
        return this;
    }

    public TkpdOkHttpBuilder addAuthenticator(Authenticator authenticator){
        if(authenticator != null) {
            builder.authenticator(authenticator);
        }
        return this;
    }

    private TkpdOkHttpBuilder addDebugInterceptor() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            this.addInterceptor(new ChuckerInterceptor(context));
            this.addInterceptor(getHttpLoggingInterceptor());
        }

        return this;
    }

    public TkpdOkHttpBuilder setOkHttpRetryPolicy() {
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);

        return this;
    }

    public TkpdOkHttpBuilder addLegacyChiper(){
        // Add legacy cipher suite for Android 4
        List<CipherSuite> cipherSuites = ConnectionSpec.MODERN_TLS.cipherSuites();
        if (!cipherSuites.contains(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)) {
            cipherSuites = new ArrayList(cipherSuites);
            cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA);
            cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA);
        }
        final ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .cipherSuites(cipherSuites.toArray(new CipherSuite[0]))
                .build();
        builder.connectionSpecs(Collections.singletonList(spec));
        return this;
    }

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.NONE;
        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingLevel = HttpLoggingInterceptor.Level.BODY;
        }
        return new HttpLoggingInterceptor().setLevel(loggingLevel);
    }

    public OkHttpClient build() {
        setOkHttpRetryPolicy();
        addDebugInterceptor();
        addLegacyChiper();
        return builder.build();
    }
}
