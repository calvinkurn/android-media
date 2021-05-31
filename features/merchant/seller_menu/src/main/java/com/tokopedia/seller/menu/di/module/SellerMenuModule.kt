package com.tokopedia.seller.menu.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.gm.common.constant.GMCommonUrl
import com.tokopedia.gm.common.data.interceptor.PowerMerchantSubscribeInterceptor
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.di.scope.SellerMenuScope
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class SellerMenuModule {

    @SellerMenuScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @SellerMenuScope
    @Provides
    fun provideUserGrqphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @SellerMenuScope
    @Provides
    fun provideFirebaseRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @SellerMenuScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @SellerMenuScope
    @Provides
    fun provideTkpdAuthInterceptor(
        @ApplicationContext context: Context,
        networkRouter: NetworkRouter,
        userSession: UserSessionInterface
    ): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }


    @SellerMenuScope
    @Provides
    fun providePowerMerchantSubscribeInterceptor(userSessionInterface: UserSessionInterface): PowerMerchantSubscribeInterceptor {
        return PowerMerchantSubscribeInterceptor(userSessionInterface)
    }

    @SellerMenuScope
    @Provides
    fun provideOkHttpClient(tkpdAuthInterceptor: TkpdAuthInterceptor,
                            powerMerchantSubscribeInterceptor: PowerMerchantSubscribeInterceptor): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .addInterceptor(HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java))
            .addInterceptor(tkpdAuthInterceptor)
            .addInterceptor(powerMerchantSubscribeInterceptor)

        return builder.build()
    }

    @SellerMenuScope
    @Provides
    fun provideVoteRetrofit(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder.baseUrl(GMCommonUrl.BASE_URL).client(okHttpClient).build()
    }

    @SellerMenuScope
    @Provides
    fun provideGMCommonApi(retrofit: Retrofit): GMCommonApi {
        return retrofit.create(GMCommonApi::class.java)
    }

    @SellerMenuScope
    @Provides
    fun provideSellerMenuTracker(userSession: UserSessionInterface): SellerMenuTracker {
        val analytics = TrackApp.getInstance().gtm
        return SellerMenuTracker(analytics, userSession)
    }
}