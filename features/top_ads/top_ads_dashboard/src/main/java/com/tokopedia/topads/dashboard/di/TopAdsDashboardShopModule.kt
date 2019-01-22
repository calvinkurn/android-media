package com.tokopedia.topads.dashboard.di

import android.content.Context

import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor
import com.tokopedia.seller.shop.common.di.ShopQualifier
import com.tokopedia.shop.common.constant.ShopCommonUrl
import com.tokopedia.shop.common.data.interceptor.ShopAuthInterceptor
import com.tokopedia.topads.common.data.util.CacheApiTKPDResponseValidator

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@TopAdsDashboardScope
class TopAdsDashboardShopModule {
    @ShopQualifier
    @Provides
    fun provideApiCacheInterceptor(): CacheApiInterceptor {
        return CacheApiInterceptor(CacheApiTKPDResponseValidator(TkpdV4ResponseError::class.java))
    }

    @ShopQualifier
    @Provides
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(TkpdV4ResponseError::class.java)
    }

    @Provides
    fun provideShopAuthInterceptor(@ApplicationContext context: Context,
                                   abstractionRouter: AbstractionRouter,
                                   userSession: UserSession): ShopAuthInterceptor {

        return ShopAuthInterceptor(context, abstractionRouter, userSession)
    }

    @ShopQualifier
    @Provides
    fun provideOkHttpClient(shopAuthInterceptor: ShopAuthInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            @ShopQualifier errorResponseInterceptor: ErrorResponseInterceptor,
                            cacheApiInterceptor: CacheApiInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(shopAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }

    @ShopQualifier
    @Provides
    fun provideRetrofit(@ShopQualifier okHttpClient: OkHttpClient,
                        retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_URL).client(okHttpClient).build()
    }
}
