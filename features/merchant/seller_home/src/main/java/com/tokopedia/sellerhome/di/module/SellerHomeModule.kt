package com.tokopedia.sellerhome.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.settings.analytics.SettingFreeShippingTracker
import com.tokopedia.sellerhome.settings.analytics.SettingShopOperationalTracker
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

@Module
class SellerHomeModule {

    @SellerHomeScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @SellerHomeScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

    @SellerHomeScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl =
            FirebaseRemoteConfigImpl(context)

    @SellerHomeScope
    @Provides
    fun provideSellerHomeRemoteConfig(remoteConfig: FirebaseRemoteConfigImpl): SellerHomeRemoteConfig {
        return SellerHomeRemoteConfig(remoteConfig)
    }

    @SellerHomeScope
    @Provides
    fun provideFreeShippingTracker(userSession: UserSessionInterface): SettingFreeShippingTracker {
        val analytics = TrackApp.getInstance().gtm
        return SettingFreeShippingTracker(analytics, userSession)
    }

    @SellerHomeScope
    @Provides
    fun provideOperationalTracker(): SettingShopOperationalTracker {
        val analytics = TrackApp.getInstance().gtm
        return SettingShopOperationalTracker(analytics)
    }
}