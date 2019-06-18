package com.tokopedia.profile.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.affiliatecommon.data.network.TOPADS_BASE_URL
import com.tokopedia.affiliatecommon.data.network.TopAdsApi
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * @author by milhamj on 10/17/18.
 */
@Module
class ProfileNetworkModule {
    @ProfileScope
    @Provides
    fun provideRetrofit(@ApplicationContext context: Context, userSession: UserSession): Retrofit {
        if ((context is NetworkRouter).not()) {
            throw IllegalStateException("Application must implement "
                    .plus(NetworkRouter::class.java.simpleName)
            )
        }

        return CommonNetwork.createRetrofit(
                context,
                TOPADS_BASE_URL,
                context as NetworkRouter,
                userSession
        )
    }

    @ProfileScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }

    @ProfileScope
    @Provides
    fun provideTopAdsApi(retrofit: Retrofit): TopAdsApi {
        return retrofit.create(TopAdsApi::class.java)
    }
}