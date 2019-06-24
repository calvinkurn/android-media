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
import com.tokopedia.search.di.module.CacheApiInterceptorModule;
import com.tokopedia.search.di.module.FingerprintInterceptorModule;
import com.tokopedia.search.di.module.SearchRetrofitBuilderModule;
import com.tokopedia.search.di.module.TkpdBaseInterceptorModule;
import com.tokopedia.search.di.qualifier.AceQualifier;
import com.tokopedia.search.di.qualifier.SearchQualifier;
import com.tokopedia.search.result.network.interceptor.DebugInterceptor;
import com.tokopedia.search.result.network.interceptor.DebugInterceptorModule;
import com.tokopedia.search.result.network.interceptor.TopAdsAuthInterceptor;
import com.tokopedia.search.result.network.interceptor.TopAdsAuthInterceptorModule;
import com.tokopedia.search.result.network.validator.CacheApiTKPDResponseValidator;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@SearchScope
@Module(includes = {
        SearchRetrofitBuilderModule.class,
        TopAdsAuthInterceptorModule.class,
        FingerprintInterceptorModule.class,
        TkpdBaseInterceptorModule.class,
        DebugInterceptorModule.class,
        CacheApiInterceptorModule.class
})
public class BrowseApiModule {

    @SearchScope
    @Provides
    @AceQualifier
    BrowseApi provideAceService(@AceQualifier Retrofit retrofit) {
        return retrofit.create(BrowseApi.class);
    }

    @SearchScope
    @Provides
    @AceQualifier
    Retrofit provideAceRetrofit(@AceQualifier OkHttpClient okHttpClient,
                                       @SearchQualifier Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(SearchConstant.BaseUrl.ACE_DOMAIN).client(okHttpClient).build();
    }

    @SearchScope
    @Provides
    @AceQualifier
    OkHttpClient provideOkHttpClientAceQualifier(@ApplicationContext Context context,
                                                 TopAdsAuthInterceptor topAdsAuthInterceptor,
                                                 FingerprintInterceptor fingerprintInterceptor,
                                                 TkpdBaseInterceptor tkpdBaseInterceptor,
                                                 DebugInterceptor debugInterceptor,
                                                 CacheApiInterceptor cacheApiInterceptor,
                                                 HttpLoggingInterceptor httpLoggingInterceptor) {

        cacheApiInterceptor.setResponseValidator(new CacheApiTKPDResponseValidator<>(TkpdV4ResponseError.class));

        OkHttpClient client = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder())
                .addInterceptor(httpLoggingInterceptor)
                .build();

        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context, client.newBuilder())
                .addInterceptor(topAdsAuthInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdBaseInterceptor)
                .addInterceptor(cacheApiInterceptor);

        if(GlobalConfig.isAllowDebuggingTools()) {
            tkpdOkHttpBuilder.addInterceptor(debugInterceptor);
        }

        return tkpdOkHttpBuilder.build();
    }
}
