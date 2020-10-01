package com.tokopedia.favorite.di.modul

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor
import com.tokopedia.favorite.data.source.apis.interceptor.TopAdsAuthInterceptor
import com.tokopedia.favorite.data.source.apis.response.TopAdsResponseError
import com.tokopedia.favorite.data.source.apis.service.TopAdsService
import com.tokopedia.favorite.data.source.apis.validator.CacheApiTKPDResponseValidator
import com.tokopedia.favorite.di.qualifier.TopAdsQualifier
import com.tokopedia.favorite.di.scope.FavoriteScope
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.utils.TkpdOkHttpBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@FavoriteScope
@Module(includes = [
    FavoriteRetrofitBuilderModule::class,
    CacheApiInterceptorModule::class,
    TopAdsAuthInterceptorModule::class,
    FingerprintInterceptorModule::class
])
class TopAdsServiceModule {
    var TOPADS_DOMAIN = "https://ta.tokopedia.com/"

    @TopAdsQualifier
    @FavoriteScope
    @Provides
    fun proviceTopAdsService(@TopAdsQualifier topAdsRetrofit: Retrofit): TopAdsService {
        return topAdsRetrofit.create(TopAdsService::class.java)
    }

    @TopAdsQualifier
    @FavoriteScope
    @Provides
    fun provideTopAdsRetrofit(@TopAdsQualifier okHttpClient: OkHttpClient,
                              @TopAdsQualifier retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(TOPADS_DOMAIN).client(okHttpClient).build()
    }

    @TopAdsQualifier
    @FavoriteScope
    @Provides
    fun provideOkHttpClientTopAdsAuth(
            @ApplicationContext context: Context,
            fingerprintInterceptor: FingerprintInterceptor,
            topAdsAuthInterceptor: TopAdsAuthInterceptor,
            @TopAdsQualifier errorResponseInterceptor: ErrorResponseInterceptor,
            cacheApiInterceptor: CacheApiInterceptor,
            httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {

        cacheApiInterceptor.setResponseValidator(
                CacheApiTKPDResponseValidator(TopAdsResponseError::class.java))
        val client = TkpdOkHttpBuilder(context, OkHttpClient.Builder())
                .addInterceptor(httpLoggingInterceptor)
                .build()
        return TkpdOkHttpBuilder(context, client.newBuilder())
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(topAdsAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .setOkHttpRetryPolicy()
                .build()
    }

    @TopAdsQualifier
    @FavoriteScope
    @Provides
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(TopAdsResponseError::class.java)
    }
}
