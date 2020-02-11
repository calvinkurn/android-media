package com.tokopedia.abstraction.common.di.module.net;

import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * @author ricoharisin on 3/23/17.
 */

@Module(includes = {InterceptorModule.class})
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

}
