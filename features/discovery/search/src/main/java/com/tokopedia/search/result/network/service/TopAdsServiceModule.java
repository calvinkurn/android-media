package com.tokopedia.search.result.network.service;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.search.di.module.CacheApiInterceptorModule;
import com.tokopedia.search.di.module.FingerprintInterceptorModule;
import com.tokopedia.search.di.module.SearchRetrofitBuilderModule;
import com.tokopedia.search.di.qualifier.SearchQualifier;
import com.tokopedia.search.di.qualifier.TopAdsQualifier;
import com.tokopedia.search.result.network.converterfactory.GeneratedHostConverter;
import com.tokopedia.search.result.network.interceptor.TopAdsAuthInterceptor;
import com.tokopedia.search.result.network.interceptor.TopAdsAuthInterceptorModule;
import com.tokopedia.search.result.network.response.TopAdsResponseError;
import com.tokopedia.search.result.network.validator.CacheApiTKPDResponseValidator;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@SearchScope
@Module(includes = {
        SearchRetrofitBuilderModule.class,
        CacheApiInterceptorModule.class,
        TopAdsAuthInterceptorModule.class,
        FingerprintInterceptorModule.class
})
public class TopAdsServiceModule {

    @TopAdsQualifier
    @SearchScope
    @Provides
    public TopAdsService proviceTopAdsService(@TopAdsQualifier Retrofit topAdsRetrofit) {
        return topAdsRetrofit.create(TopAdsService.class);
    }

    @TopAdsQualifier
    @SearchScope
    @Provides
    public Retrofit provideTopAdsRetrofit(@TopAdsQualifier OkHttpClient okHttpClient,
                                          @SearchQualifier Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(SearchConstant.BaseUrl.TOPADS_DOMAIN).client(okHttpClient).build();
    }

    @TopAdsQualifier
    @SearchScope
    @Provides
    public OkHttpClient provideOkHttpClientTopAdsAuth(@ApplicationContext Context context,
                                                      FingerprintInterceptor fingerprintInterceptor,
                                                      TopAdsAuthInterceptor topAdsAuthInterceptor,
                                                      @TopAdsQualifier ErrorResponseInterceptor errorResponseInterceptor,
                                                      CacheApiInterceptor cacheApiInterceptor,
                                                      HttpLoggingInterceptor httpLoggingInterceptor) {

        cacheApiInterceptor.setResponseValidator(new CacheApiTKPDResponseValidator<>(TopAdsResponseError.class));

        OkHttpClient client = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder())
                .addInterceptor(httpLoggingInterceptor)
                .build();

        return new TkpdOkHttpBuilder(context, client.newBuilder())
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(topAdsAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .setOkHttpRetryPolicy()
                .build();
    }

    @TopAdsQualifier
    @SearchScope
    @Provides
    public ErrorResponseInterceptor provideErrorResponseInterceptor() {
        return new ErrorResponseInterceptor(TopAdsResponseError.class);
    }
}
