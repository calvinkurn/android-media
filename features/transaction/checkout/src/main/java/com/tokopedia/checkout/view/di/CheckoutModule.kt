package com.tokopedia.checkout.view.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.checkout.R
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.mapper.CheckoutMapper
import com.tokopedia.checkout.domain.mapper.ICheckoutMapper
import com.tokopedia.checkout.domain.mapper.IShipmentMapper
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase.Companion.CHANGE_SHIPPING_ADDRESS_MUTATION
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase.Companion.CHECKOUT_MUTATION
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormGqlUseCase.Companion.SHIPMENT_ADDRESS_FORM_QUERY
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase.Companion.SAVE_SHIPMENT_STATE_MUTATION
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentFragment
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticcart.domain.executor.MainScheduler
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.data.analytics.CodAnalytics
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.di.PurchasePlatformBaseModule
import com.tokopedia.purchase_platform.common.di.PurchasePlatformNetworkModule
import com.tokopedia.purchase_platform.common.feature.editaddress.di.PeopleAddressNetworkModule
import com.tokopedia.purchase_platform.common.feature.editaddress.domain.usecase.EditAddressUseCase
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.insurance.InsuranceItemActionListener
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.DefaultSchedulers
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import rx.subscriptions.CompositeSubscription
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-08-26.
 */

@Module(includes = [
    PeopleAddressNetworkModule::class,
    PromoCheckoutModule::class,
    PurchasePlatformNetworkModule::class,
    PurchasePlatformBaseModule::class
])
class CheckoutModule constructor(val shipmentFragment: ShipmentFragment) {

    @Provides
    @CheckoutScope
    fun provideICheckoutMapper(): ICheckoutMapper {
        return CheckoutMapper()
    }

    @Provides
    @CheckoutScope
    fun provideIShipmentMapper(): IShipmentMapper {
        return ShipmentMapper()
    }

    @Provides
    @CheckoutScope
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context,
                                             mapper: CheckPromoStackingCodeMapper): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources, mapper)
    }

    @Provides
    @CheckoutScope
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }

    @Provides
    @CheckoutScope
    @Named(SubmitHelpTicketUseCase.QUERY_NAME)
    fun provideSubmitHelpTicketUseCaseQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.purchase_platform.common.R.raw.submit_help_ticket)
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
    fun provideShipmentPresenter(compositeSubscription: CompositeSubscription,
                                 checkoutGqlUseCase: CheckoutGqlUseCase,
                                 getShipmentAddressFormGqlUseCase: GetShipmentAddressFormGqlUseCase,
                                 editAddressUseCase: EditAddressUseCase,
                                 changeShippingAddressGqlUseCase: ChangeShippingAddressGqlUseCase,
                                 saveShipmentStateGqlUseCase: SaveShipmentStateGqlUseCase,
                                 codCheckoutUseCase: CodCheckoutUseCase,
                                 ratesUseCase: GetRatesUseCase,
                                 ratesApiUseCase: GetRatesApiUseCase,
                                 stateConverter: RatesResponseStateConverter,
                                 clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
                                 submitHelpTicketUseCase: SubmitHelpTicketUseCase,
                                 shippingCourierConverter: ShippingCourierConverter,
                                 userSessionInterface: UserSessionInterface,
                                 analyticsPurchaseProtection: CheckoutAnalyticsPurchaseProtection,
                                 codAnalytics: CodAnalytics,
                                 checkoutAnalytics: CheckoutAnalyticsCourierSelection,
                                 getInsuranceCartUseCase: GetInsuranceCartUseCase,
                                 shipmentDataConverter: ShipmentDataConverter,
                                 releaseBookingUseCase: ReleaseBookingUseCase,
                                 validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase): ShipmentContract.Presenter {
        return ShipmentPresenter(compositeSubscription,
                checkoutGqlUseCase, getShipmentAddressFormGqlUseCase,
                editAddressUseCase, changeShippingAddressGqlUseCase,
                saveShipmentStateGqlUseCase,
                ratesUseCase, ratesApiUseCase,
                codCheckoutUseCase, clearCacheAutoApplyStackUseCase, submitHelpTicketUseCase,
                stateConverter, shippingCourierConverter, shipmentFragment, userSessionInterface,
                analyticsPurchaseProtection, codAnalytics, checkoutAnalytics, getInsuranceCartUseCase,
                shipmentDataConverter, releaseBookingUseCase, validateUsePromoRevampUseCase)
    }

    @Provides
    @CheckoutScope
    fun provideShipmentAdapterActionListener(): ShipmentAdapterActionListener {
        return shipmentFragment
    }

    @Provides
    @CheckoutScope
    fun provideInsuranceItemActionListener(): InsuranceItemActionListener {
        return shipmentFragment
    }

    @Provides
    @CheckoutScope
    fun provideTrackingPromo(): TrackingPromoCheckoutUtil {
        return TrackingPromoCheckoutUtil()
    }

    @Provides
    @CheckoutScope
    @Named(SHIPMENT_ADDRESS_FORM_QUERY)
    fun provideGetShipmentAddressFormQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.shipment_address_form_query)
    }

    @Provides
    @CheckoutScope
    @Named(SAVE_SHIPMENT_STATE_MUTATION)
    fun provideSaveShipmentStateMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.save_shipment_state_mutation)
    }

    @Provides
    @CheckoutScope
    @Named(CHANGE_SHIPPING_ADDRESS_MUTATION)
    fun provideChangeShippingAddressMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.change_shipping_address_mutation)
    }

    @Provides
    @CheckoutScope
    @Named(CHECKOUT_MUTATION)
    fun provideCheckoutMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.checkout_mutation)
    }

}