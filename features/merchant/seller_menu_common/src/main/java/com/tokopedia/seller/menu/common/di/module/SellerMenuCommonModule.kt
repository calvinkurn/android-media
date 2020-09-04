package com.tokopedia.seller.menu.common.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.analytics.SettingFreeShippingTracker
import com.tokopedia.seller.menu.common.di.scope.SellerMenuCommonScope
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class SellerMenuCommonModule {

    @SellerMenuCommonScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @SellerMenuCommonScope
    @Provides
    fun provideFreeShippingTracker(userSession: UserSessionInterface): SettingFreeShippingTracker {
        val analytics = TrackApp.getInstance().gtm
        return SettingFreeShippingTracker(analytics, userSession)
    }

    @SellerMenuCommonScope
    @Provides
    fun provideSellerMenuTracker(userSession: UserSessionInterface): SellerMenuTracker {
        val analytics = TrackApp.getInstance().gtm
        return SellerMenuTracker(analytics, userSession)
    }
}