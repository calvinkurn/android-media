package com.tokopedia.checkout.view.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.checkout.R
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics
import com.tokopedia.checkout.domain.mapper.CheckoutMapper
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase.Companion.CHANGE_SHIPPING_ADDRESS_MUTATION
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase.Companion.CHECKOUT_MUTATION
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV3UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase.Companion.SAVE_SHIPMENT_STATE_MUTATION
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentFragment
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.domain.executor.MainScheduler
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.analytics.EPharmacyAnalytics
import com.tokopedia.purchase_platform.common.di.PurchasePlatformBaseModule
import com.tokopedia.purchase_platform.common.di.PurchasePlatformNetworkModule
import com.tokopedia.purchase_platform.common.feature.editaddress.di.PeopleAddressNetworkModule
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionListener
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.sellercashback.SellerCashbackListener
import com.tokopedia.purchase_platform.common.schedulers.DefaultSchedulers
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import rx.subscriptions.CompositeSubscription
import javax.inject.Named

@Module(
    includes = [
        PeopleAddressNetworkModule::class,
        PurchasePlatformNetworkModule::class,
        PurchasePlatformBaseModule::class
    ]
)
class CheckoutModule constructor(val shipmentFragment: ShipmentFragment) {

    @Provides
    @CheckoutScope
    fun provideContext(): Context = shipmentFragment.activityContext

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
    fun provideShipmentPresenter(
        compositeSubscription: CompositeSubscription,
        checkoutGqlUseCase: CheckoutGqlUseCase,
        getShipmentAddressFormV3UseCase: GetShipmentAddressFormV3UseCase,
        editAddressUseCase: EditAddressUseCase,
        changeShippingAddressGqlUseCase: ChangeShippingAddressGqlUseCase,
        saveShipmentStateGqlUseCase: SaveShipmentStateGqlUseCase,
        ratesUseCase: GetRatesUseCase,
        ratesApiUseCase: GetRatesApiUseCase,
        stateConverter: RatesResponseStateConverter,
        clearCacheAutoApplyStackUseCase: OldClearCacheAutoApplyStackUseCase,
        shippingCourierConverter: ShippingCourierConverter,
        userSessionInterface: UserSessionInterface,
        analyticsPurchaseProtection: CheckoutAnalyticsPurchaseProtection,
        checkoutAnalytics: CheckoutAnalyticsCourierSelection,
        shipmentDataConverter: ShipmentDataConverter,
        releaseBookingUseCase: ReleaseBookingUseCase,
        prescriptionIdsUseCase: GetPrescriptionIdsUseCase,
        epharmacyUseCase: EPharmacyPrepareProductsGroupUseCase,
        validateUsePromoRevampUseCase: OldValidateUsePromoRevampUseCase,
        gson: Gson,
        executorSchedulers: ExecutorSchedulers,
        eligibleForAddressUseCase: EligibleForAddressUseCase
    ): ShipmentContract.Presenter {
        return ShipmentPresenter(
            compositeSubscription,
            checkoutGqlUseCase, getShipmentAddressFormV3UseCase,
            editAddressUseCase, changeShippingAddressGqlUseCase,
            saveShipmentStateGqlUseCase,
            ratesUseCase, ratesApiUseCase,
            clearCacheAutoApplyStackUseCase,
            stateConverter, shippingCourierConverter, shipmentFragment, userSessionInterface,
            analyticsPurchaseProtection, checkoutAnalytics,
            shipmentDataConverter, releaseBookingUseCase, prescriptionIdsUseCase,
            epharmacyUseCase, validateUsePromoRevampUseCase, gson,
            executorSchedulers, eligibleForAddressUseCase
        )
    }

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
    @Named(SAVE_SHIPMENT_STATE_MUTATION)
    fun provideSaveShipmentStateMutation(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.save_shipment_state_mutation)
    }

    @Provides
    @CheckoutScope
    @Named(CHANGE_SHIPPING_ADDRESS_MUTATION)
    fun provideChangeShippingAddressMutation(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.change_shipping_address_mutation)
    }

    @Provides
    @CheckoutScope
    @Named(CHECKOUT_MUTATION)
    fun provideCheckoutMutation(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.checkout_mutation)
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
