package com.tokopedia.topads.dashboard.di

import android.content.Context
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.interceptor.TopAdsAuthInterceptor
import com.tokopedia.topads.common.data.interceptor.TopAdsResponseError
import com.tokopedia.topads.common.data.util.CacheApiTKPDResponseValidator
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@TopAdsDashboardScope
@Module
class TopAdsDashboardNetworkModule {

    @TopAdsDashboardScope
    @Provides
    fun provideTopAdsAuthTempInterceptor(@ApplicationContext context: Context,
                                         abstractionRouter: AbstractionRouter): TopAdsAuthInterceptor {
        return TopAdsAuthInterceptor(context, abstractionRouter)
    }

    @TopAdsDashboardScope
    @Provides
    fun provideApiCacheInterceptor(): CacheApiInterceptor {
        return CacheApiInterceptor(CacheApiTKPDResponseValidator(TopAdsResponseError::class.java))
    }

    @TopAdsDashboardQualifier
    @TopAdsDashboardScope
    @Provides
    fun provideErrorInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(TopAdsResponseError::class.java)
    }

    @TopAdsDashboardQualifier
    @Provides
    fun provideOkHttpClient(topAdsAuthInterceptor: TopAdsAuthInterceptor,
                            @TopAdsDashboardQualifier errorResponseInterceptor: ErrorResponseInterceptor,
                            cacheApiInterceptor: CacheApiInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(topAdsAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .build()
    }

    @TopAdsDashboardQualifier
    @TopAdsDashboardScope
    @Provides
    fun provideRetrofit(@TopAdsDashboardQualifier okHttpClient: OkHttpClient,
                        retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(TopAdsCommonConstant.BASE_DOMAIN_URL).client(okHttpClient).build()
    }
}