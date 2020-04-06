package com.tokopedia.favorite.di.modul;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.favorite.data.source.apis.interceptor.TopAdsAuthInterceptor;
import com.tokopedia.favorite.data.source.apis.response.TopAdsResponseError;
import com.tokopedia.favorite.data.source.apis.service.TopAdsService;
import com.tokopedia.favorite.data.source.apis.validator.CacheApiTKPDResponseValidator;
import com.tokopedia.favorite.di.qualifier.TopAdsQualifier;
import com.tokopedia.favorite.di.scope.FavoriteScope;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@FavoriteScope
@Module(includes = {
        FavoriteRetrofitBuilderModule.class,
        CacheApiInterceptorModule.class,
        TopAdsAuthInterceptorModule.class,
        FingerprintInterceptorModule.class
})
public class TopAdsServiceModule {

    String TOPADS_DOMAIN = "https://ta.tokopedia.com/";

    @TopAdsQualifier
    @FavoriteScope
    @Provides
    public TopAdsService proviceTopAdsService(@TopAdsQualifier Retrofit topAdsRetrofit) {
        return topAdsRetrofit.create(TopAdsService.class);
    }

    @TopAdsQualifier
    @FavoriteScope
    @Provides
    public Retrofit provideTopAdsRetrofit(@TopAdsQualifier OkHttpClient okHttpClient,
                                          @TopAdsQualifier Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TOPADS_DOMAIN).client(okHttpClient).build();
    }

    @TopAdsQualifier
    @FavoriteScope
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
    @FavoriteScope
    @Provides
    public ErrorResponseInterceptor provideErrorResponseInterceptor() {
        return new ErrorResponseInterceptor(TopAdsResponseError.class);
    }
}
