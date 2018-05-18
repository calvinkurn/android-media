package com.tokopedia.network.utils;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.network.interceptor.TkpdBaseInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ricoharisin on 2/28/17.
 */

public class TkpdOkHttpBuilder {

    private OkHttpClient.Builder builder;
    private Context context;

    public TkpdOkHttpBuilder(Context context, OkHttpClient.Builder builder) {
        this.builder = builder;
        this.context = context;
    }

    public OkHttpClient.Builder getBuilder() {
        return builder;
    }

    public TkpdOkHttpBuilder addInterceptor(Interceptor interceptor) {
        builder.addInterceptor(interceptor);
        return this;
    }

    private TkpdOkHttpBuilder addDebugInterceptor() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            this.addInterceptor(new ChuckInterceptor(context));
            this.addInterceptor(getHttpLoggingInterceptor());
        }

        return this;
    }

    public TkpdOkHttpBuilder setOkHttpRetryPolicy(OkHttpRetryPolicy retryPolicy) {
        builder.readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS);
        builder.connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS);
        builder.writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS);
        for (Interceptor interceptor : builder.interceptors()) {
            if (interceptor instanceof TkpdBaseInterceptor) {
                ((TkpdBaseInterceptor) interceptor).setMaxRetryAttempt(retryPolicy.maxRetryAttempt);
            }
        }

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
        addDebugInterceptor();
        return builder.build();
    }
}
