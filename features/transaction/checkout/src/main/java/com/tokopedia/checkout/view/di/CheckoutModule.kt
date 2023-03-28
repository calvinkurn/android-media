package com.tokopedia.checkout.view.di

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics
import com.tokopedia.checkout.domain.mapper.CheckoutMapper
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.ShipmentContract.AnalyticsActionListener
import com.tokopedia.checkout.view.ShipmentFragment
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter
import com.tokopedia.logisticcart.domain.executor.MainScheduler
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.purchase_platform.common.analytics.EPharmacyAnalytics
import com.tokopedia.purchase_platform.common.di.PurchasePlatformBaseModule
import com.tokopedia.purchase_platform.common.di.PurchasePlatformNetworkModule
import com.tokopedia.purchase_platform.common.feature.editaddress.di.PeopleAddressNetworkModule
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionListener
import com.tokopedia.purchase_platform.common.feature.sellercashback.SellerCashbackListener
import com.tokopedia.purchase_platform.common.schedulers.DefaultSchedulers
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import rx.subscriptions.CompositeSubscription

@Module(
    includes = [
        PeopleAddressNetworkModule::class,
        PurchasePlatformNetworkModule::class,
        PurchasePlatformBaseModule::class
    ]
)
class CheckoutModule constructor(private val shipmentFragment: ShipmentFragment) {

    @Provides
    @CheckoutScope
    fun provideICheckoutMapper(gson: Gson): CheckoutMapper {
        return CheckoutMapper(gson)
    }

    @Provides
    @CheckoutScope
    fun provideShipmentDataRequestConverter(gson: Gson): ShipmentDataRequestConverter {
        return ShipmentDataRequestConverter(gson)
    }

    @Provides
    @CheckoutScope
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }

    @Provides
    @CheckoutScope
    fun provideScheduler(): SchedulerProvider {
        return MainScheduler()
    }

    @Provides
    @CheckoutScope
    fun provideExecutorSchedulers(): ExecutorSchedulers = DefaultSchedulers

    @Provides
    @CheckoutScope
    fun provideAnalyticsListener(): AnalyticsActionListener = shipmentFragment

    @Provides
    @CheckoutScope
    fun provideShipmentAdapterActionListener(): ShipmentAdapterActionListener {
        return shipmentFragment
    }

    @Provides
    @CheckoutScope
    fun provideSellerCashbackListener(): SellerCashbackListener {
        return shipmentFragment
    }

    @Provides
    @CheckoutScope
    fun provideUploadPrescriptionListener(): UploadPrescriptionListener {
        return shipmentFragment
    }

    @Provides
    @CheckoutScope
    fun provideCheckoutTradeInAnalytics(userSession: UserSessionInterface): CheckoutTradeInAnalytics {
        return CheckoutTradeInAnalytics(userSession.userId)
    }

    @Provides
    @CheckoutScope
    fun provideEPharmacyAnalytics(userSession: UserSessionInterface): EPharmacyAnalytics {
        return EPharmacyAnalytics(userSession.userId)
    }
}
