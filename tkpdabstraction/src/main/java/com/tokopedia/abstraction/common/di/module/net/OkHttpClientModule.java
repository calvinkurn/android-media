package com.tokopedia.abstraction.common.di.module.net;

import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.TkpdOkHttpBuilder;
import com.tokopedia.abstraction.common.network.interceptor.DebugInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdBaseInterceptor;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * @author  ricoharisin on 3/23/17.
 */

@Module(includes={InterceptorModule.class})
public class OkHttpClientModule {

    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder okHttpClientBuilder) {
        return okHttpClientBuilder.build();
    }

    @ApplicationScope
    @Provides
    public OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }

    @ApplicationScope
    @Provides
    public OkHttpClient.Builder provideOkHttpClientBuilder() {
        return new OkHttpClient().newBuilder();
    }
}
