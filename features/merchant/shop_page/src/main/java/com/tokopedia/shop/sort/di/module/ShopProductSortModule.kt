package com.tokopedia.shop.sort.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.network.NetworkRouter
import com.tokopedia.shop.common.constant.ShopUrl
import com.tokopedia.shop.common.data.interceptor.ShopAuthInterceptor
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.sort.data.repository.ShopProductSortRepositoryImpl
import com.tokopedia.shop.sort.data.source.cloud.ShopProductSortCloudDataSource
import com.tokopedia.shop.sort.data.source.cloud.api.ShopAceApi
import com.tokopedia.shop.sort.di.scope.ShopProductSortScope
import com.tokopedia.shop.sort.domain.repository.ShopProductSortRepository
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
class ShopProductSortModule {
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    fun provideOkHttpClient(shopAuthInterceptor: ShopAuthInterceptor,
                            @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
                            errorResponseInterceptor: HeaderErrorResponseInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(shopAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }

    @ShopProductSortScope
    @Provides
    fun provideShopAceRetrofit(okHttpClient: OkHttpClient,
                               retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(ShopUrl.BASE_ACE_URL).client(okHttpClient).build()
    }

    @ShopProductSortScope
    @Provides
    fun provideShopAceApi(retrofit: Retrofit): ShopAceApi {
        return retrofit.create(ShopAceApi::class.java)
    }

    @ShopProductSortScope
    @Provides
    fun provideShopProductSortRepository(shopProductDataSource: ShopProductSortCloudDataSource): ShopProductSortRepository {
        return ShopProductSortRepositoryImpl(shopProductDataSource)
    }

    @ShopProductSortScope
    @Provides
    fun provideShopProductSortMapper(): ShopProductSortMapper {
        return ShopProductSortMapper()
    }

    @ShopProductSortScope
    @Provides
    fun provideUserSessionInterface(@ShopPageContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}