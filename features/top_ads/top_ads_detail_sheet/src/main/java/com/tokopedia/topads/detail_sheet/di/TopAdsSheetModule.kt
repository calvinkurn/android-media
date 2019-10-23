package com.tokopedia.topads.detail_sheet.di

import android.content.Context
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.interceptor.TopAdsAuthInterceptor
import com.tokopedia.topads.common.data.interceptor.TopAdsResponseError
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Author errysuprayogi on 22,October,2019
 */
@TopAdsSheetScope
@Module
class TopAdsSheetModule {

    @TopAdsSheetScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @Provides
    internal fun provideRetrofit(okHttpClient: OkHttpClient,
                                 retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(TopAdsCommonConstant.BASE_DOMAIN_URL).client(okHttpClient).build()
    }

    @Provides
    fun provideErrorInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(TopAdsResponseError::class.java)
    }

    @Provides
    fun provideOkHttpClient(topAdsAuthInterceptor: TopAdsAuthInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            errorResponseInterceptor: ErrorResponseInterceptor,
                            cacheApiInterceptor: CacheApiInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(topAdsAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }

    @Provides
    fun provideTopAdsAuthInterceptor(@ApplicationContext context: Context,
                                         abstractionRouter: AbstractionRouter): TopAdsAuthInterceptor {
        return TopAdsAuthInterceptor(context, abstractionRouter)
    }
}

