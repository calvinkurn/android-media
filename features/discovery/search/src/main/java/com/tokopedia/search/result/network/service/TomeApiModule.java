package com.tokopedia.search.result.network.service;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdBaseInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.search.di.module.SearchRetrofitBuilderModule;
import com.tokopedia.search.di.qualifier.SearchQualifier;
import com.tokopedia.search.di.qualifier.TomeQualifier;
import com.tokopedia.search.result.network.interceptor.DebugInterceptor;
import com.tokopedia.search.result.network.validator.CacheApiTKPDResponseValidator;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@SearchScope
@Module(includes = {
        SearchRetrofitBuilderModule.class
})
public class TomeApiModule {

    @SearchScope
    @Provides
    @TomeQualifier
    TomeApi provideTomeApi(@TomeQualifier Retrofit retrofit) {
        return retrofit.create(TomeApi.class);
    }

    @SearchScope
    @Provides
    @TomeQualifier
    Retrofit provideTomeRetrofit(@TomeQualifier OkHttpClient okHttpClient,
                                 @SearchQualifier Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(SearchConstant.BaseUrl.TOME_DOMAIN).client(okHttpClient).build();
    }

    @SearchScope
    @Provides
    @TomeQualifier
    OkHttpClient provideOkHttpClientTomeQualifier(@ApplicationContext Context context,
                                                  FingerprintInterceptor fingerprintInterceptor,
                                                  TkpdBaseInterceptor tkpdBaseInterceptor,
                                                  DebugInterceptor debugInterceptor,
                                                  CacheApiInterceptor cacheApiInterceptor) {

        cacheApiInterceptor.setResponseValidator(new CacheApiTKPDResponseValidator<>(TkpdV4ResponseError.class));

        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder())
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(tkpdBaseInterceptor);

        if(GlobalConfig.isAllowDebuggingTools()) {
            tkpdOkHttpBuilder.addInterceptor(debugInterceptor);
        }

        return tkpdOkHttpBuilder.build();
    }
}
