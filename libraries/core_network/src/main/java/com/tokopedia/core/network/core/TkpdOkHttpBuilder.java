package com.tokopedia.core.network.core;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.constant.ConstantCoreNetwork;
import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBaseInterceptor;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by ricoharisin on 2/28/17.
 */

@Deprecated
public class TkpdOkHttpBuilder {

    private OkHttpClient.Builder builder;

    public TkpdOkHttpBuilder(OkHttpClient.Builder builder) {
        this.builder = builder;
    }

    public OkHttpClient.Builder getBuilder() {
        return builder;
    }

    public TkpdOkHttpBuilder addInterceptor(Interceptor interceptor) {
        builder.addInterceptor(interceptor);
        return this;
    }

    public TkpdOkHttpBuilder addNetworkInterceptor(Interceptor interceptor) {
        builder.addNetworkInterceptor(interceptor);
        return this;
    }

    public TkpdOkHttpBuilder addDebugInterceptor() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            LocalCacheHandler cache = new LocalCacheHandler(CoreNetworkApplication.getAppContext(), ConstantCoreNetwork.CHUCK_ENABLED);
            Boolean allowLogOnNotification = cache.getBoolean(ConstantCoreNetwork.IS_CHUCK_ENABLED, false);
            this.addInterceptor(new ChuckInterceptor(CoreNetworkApplication.getAppContext())
                    .showNotification(allowLogOnNotification));
            this.addInterceptor(new DebugInterceptor());
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

    public OkHttpClient build() {
        return builder.build();
    }
}
