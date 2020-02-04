package com.tokopedia.core.network.core;

import com.chuckerteam.chucker.api.ChuckerCollector;
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.chuckerteam.chucker.api.RetentionManager;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.constant.ConstantCoreNetwork;
import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.TkpdBaseInterceptor;
import com.tokopedia.network.utils.OkHttpRetryPolicy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
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

    public TkpdOkHttpBuilder addDebugInterceptor() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            LocalCacheHandler cache = new LocalCacheHandler(CoreNetworkApplication.getAppContext(), ConstantCoreNetwork.CHUCK_ENABLED);
            Boolean allowLogOnNotification = cache.getBoolean(ConstantCoreNetwork.IS_CHUCK_ENABLED, false);
            ChuckerCollector collector = new ChuckerCollector(
                    CoreNetworkApplication.getAppContext(), allowLogOnNotification);

            this.addInterceptor(new ChuckerInterceptor(
                    CoreNetworkApplication.getAppContext(), collector));
            this.addInterceptor(new DebugInterceptor());
        }

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
        addLegacyChiper();
        return builder.build();
    }
}
