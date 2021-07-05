package com.tokopedia.topads.dashboard.di

import android.content.Context
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.network.NetworkRouter
import com.tokopedia.shop.common.constant.ShopCommonUrl
import com.tokopedia.shop.common.data.interceptor.ShopAuthInterceptor
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
class TopAdsDashboardShopModule {

    @ShopQualifier
    @Provides
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(TkpdV4ResponseError::class.java)
    }

    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    fun provideShopAuthInterceptor(@ApplicationContext context: Context,
                                   networkRouter: NetworkRouter,
                                   userSessionInterface: UserSessionInterface): ShopAuthInterceptor {

        return ShopAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ShopQualifier
    @Provides
    fun provideOkHttpClient(shopAuthInterceptor: ShopAuthInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            @ShopQualifier errorResponseInterceptor: ErrorResponseInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
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
