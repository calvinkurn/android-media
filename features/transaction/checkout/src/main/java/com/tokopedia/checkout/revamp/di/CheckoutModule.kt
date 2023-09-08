package com.tokopedia.checkout.revamp.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.purchase_platform.common.analytics.EPharmacyAnalytics
import com.tokopedia.purchase_platform.common.di.PurchasePlatformBaseModule
import com.tokopedia.purchase_platform.common.di.PurchasePlatformNetworkModule
import com.tokopedia.purchase_platform.common.feature.editaddress.di.PeopleAddressNetworkModule
import com.tokopedia.purchase_platform.common.schedulers.DefaultSchedulers
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        PeopleAddressNetworkModule::class,
        PurchasePlatformNetworkModule::class,
        PurchasePlatformBaseModule::class
    ]
)
class CheckoutModule {

    @Provides
    @ActivityScope
    fun provideExecutorSchedulers(): ExecutorSchedulers = DefaultSchedulers

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

    @Provides
    @ActivityScope
    fun provideIris(@ApplicationContext context: Context): IrisSession {
        return IrisSession(context)
    }
}
