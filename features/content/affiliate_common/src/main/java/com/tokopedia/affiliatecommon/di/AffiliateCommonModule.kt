package com.tokopedia.affiliatecommon.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.affiliatecommon.data.network.TOPADS_BASE_URL
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
import com.tokopedia.profile.data.network.TopAdsApi
import com.tokopedia.user.session.UserSession
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * @author by yfsx on 29/03/19.
 */
@Module
class AffiliateCommonModule {
    @AffiliateCommonScope
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

    @AffiliateCommonScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }

    @AffiliateCommonScope
    @Provides
    fun provideTopAdsApi(retrofit: Retrofit): TopAdsApi {
        return retrofit.create(TopAdsApi::class.java)
    }
}