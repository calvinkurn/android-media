package com.tokopedia.shop.feed.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.affiliatecommon.data.network.TOPADS_BASE_URL
import com.tokopedia.affiliatecommon.data.network.TopAdsApi
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * @author by yfsx on 08/05/19.
 */
@Module
class FeedShopNetworkModule {
    @FeedShopScope
    @Provides
    fun provideRetrofit(@ApplicationContext context: Context, userSession: UserSessionInterface): Retrofit {
        if ((context is NetworkRouter).not()) {
            throw IllegalStateException("Application must implement "
                    .plus(NetworkRouter::class.java.simpleName)
            )
        }

        return CommonNetwork.createRetrofit(
                context,
                TOPADS_BASE_URL,
                context as NetworkRouter,
                userSession as UserSession
        )
    }

    @FeedShopScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @FeedShopScope
    @Provides
    fun provideTopAdsApi(retrofit: Retrofit): TopAdsApi {
        return retrofit.create(TopAdsApi::class.java)
    }
}