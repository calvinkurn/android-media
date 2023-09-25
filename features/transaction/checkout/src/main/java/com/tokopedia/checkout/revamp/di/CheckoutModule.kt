package com.tokopedia.checkout.revamp.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics
import com.tokopedia.purchase_platform.common.analytics.EPharmacyAnalytics
import com.tokopedia.purchase_platform.common.di.PurchasePlatformBaseModule
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        PurchasePlatformBaseModule::class
    ]
)
class CheckoutModule {

    @Provides
    @ActivityScope
    fun provideCheckoutTradeInAnalytics(userSession: UserSessionInterface): CheckoutTradeInAnalytics {
        return CheckoutTradeInAnalytics(userSession.userId)
    }

    @Provides
    @ActivityScope
    fun provideEPharmacyAnalytics(userSession: UserSessionInterface): EPharmacyAnalytics {
        return EPharmacyAnalytics(userSession.userId)
    }
}
