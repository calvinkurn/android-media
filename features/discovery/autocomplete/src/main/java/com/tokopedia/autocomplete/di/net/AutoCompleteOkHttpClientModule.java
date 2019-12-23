package com.tokopedia.autocomplete.di.net;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.interceptor.DebugInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.autocomplete.di.AutoCompleteScope;
import com.tokopedia.autocomplete.di.qualifier.NoAuth;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.cacheapi.util.CacheApiResponseValidator;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@AutoCompleteScope
@Module(includes = AutoCompleteInterceptorModule.class)
public class AutoCompleteOkHttpClientModule {
    @AutoCompleteScope
    @NoAuth
    @Provides
    public OkHttpClient provideOkHttpClientNoAuth(OkHttpRetryPolicy okHttpRetryPolicy,
                                                  ChuckerInterceptor chuckInterceptor,
                                                  DebugInterceptor debugInterceptor,
                                                  CacheApiInterceptor cacheApiInterceptor,
                                                  TkpdAuthInterceptor tkpdAuthInterceptor) {

        cacheApiInterceptor.setResponseValidator(new CacheApiResponseValidator());
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(cacheApiInterceptor)
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS);

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(debugInterceptor);
            clientBuilder.addInterceptor(chuckInterceptor);
        }
        return clientBuilder.build();
    }

    @AutoCompleteScope
    @Provides
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }
}
