package com.tokopedia.productcard.options.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.network.NetworkRouter
import com.tokopedia.productcard.options.topadswishlist.TopAdsWishlistUseCase
import com.tokopedia.topads.sdk.base.AuthInterceptor
import com.tokopedia.topads.sdk.base.Config
import com.tokopedia.topads.sdk.domain.TopAdsWishlistService
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
internal class TopAdsWishlistUseCaseModule {

    @Provides
    @ProductCardOptionsScope
    fun topAdsWishlishedUseCase(topAdsWishlistService: TopAdsWishlistService): UseCase<Boolean> {
        return TopAdsWishlistUseCase(topAdsWishlistService)
    }

    @Provides
    @ProductCardOptionsScope
    fun provideTopAdsWishlistService(retrofit: Retrofit): TopAdsWishlistService {
        return retrofit.create(TopAdsWishlistService::class.java)
    }

    @Provides
    @ProductCardOptionsScope
    fun provideRetrofit(okHttpClient: OkHttpClient,
                        retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(Config.TOPADS_BASE_URL).client(okHttpClient).build()
    }

    @Provides
    @ProductCardOptionsScope
    fun provideOkHttpClient(topAdsAuthInterceptor: AuthInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(topAdsAuthInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }

    @Provides
    @ProductCardOptionsScope
    fun provideTopAdsAuthTempInterceptor(@ApplicationContext context: Context,
                                         networkRouter: NetworkRouter,
                                         userSession: UserSessionInterface): AuthInterceptor {
        return AuthInterceptor(context, networkRouter, userSession)
    }

    @Provides
    @ProductCardOptionsScope
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        if (context is NetworkRouter) {
            return context
        }

        throw RuntimeException("Application must implement " + NetworkRouter::class.java.canonicalName)
    }
}