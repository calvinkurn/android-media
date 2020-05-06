package com.tokopedia.purchase_platform.features.checkout.view.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.logisticcart.domain.executor.MainScheduler
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.data.analytics.CodAnalytics
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeFinalUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.di.*
import com.tokopedia.purchase_platform.common.domain.schedulers.DefaultSchedulers
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.features.cart.view.InsuranceItemActionListener
import com.tokopedia.purchase_platform.features.cart.view.di.CartScope
import com.tokopedia.purchase_platform.features.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.purchase_platform.features.checkout.data.api.CheckoutApi
import com.tokopedia.purchase_platform.features.checkout.data.repository.CheckoutRepository
import com.tokopedia.purchase_platform.features.checkout.data.repository.ICheckoutRepository
import com.tokopedia.purchase_platform.features.checkout.domain.mapper.IShipmentMapper
import com.tokopedia.purchase_platform.features.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.*
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentContract
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentFragment
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentPresenter
import com.tokopedia.purchase_platform.features.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.purchase_platform.features.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import rx.subscriptions.CompositeSubscription
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-08-26.
 */

@Module(includes = [
    PeopleAddressNetworkModule::class,
    PromoCheckoutModule::class,
    PurchasePlatformNetworkModule::class,
    PurchasePlatformBaseModule::class,
    PurchasePlatformCommonModule::class
])
class CheckoutModule constructor(val shipmentFragment: ShipmentFragment) {

    @Provides
    @CheckoutScope
    fun provideCheckoutApi(@PurchasePlatformQualifier retrofit: Retrofit): CheckoutApi {
        return retrofit.create(CheckoutApi::class.java)
    }

    @Provides
    @CheckoutScope
    fun provideICheckoutRepository(checkoutApi: CheckoutApi): ICheckoutRepository {
        return CheckoutRepository(checkoutApi)
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
    @CartScope
    fun provideExecutorSchedulers(): ExecutorSchedulers = DefaultSchedulers

    @Provides
    @CheckoutScope
    fun provideShipmentPresenter(checkPromoStackingCodeFinalUseCase: CheckPromoStackingCodeFinalUseCase,
                                 checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase,
                                 checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper,
                                 compositeSubscription: CompositeSubscription,
                                 checkoutUseCase: CheckoutUseCase,
                                 getShipmentAddressFormUseCase: GetShipmentAddressFormUseCase,
                                 getShipmentAddressFormOneClickShipementUseCase: GetShipmentAddressFormOneClickShipementUseCase,
                                 editAddressUseCase: EditAddressUseCase,
                                 changeShippingAddressUseCase: ChangeShippingAddressUseCase,
                                 saveShipmentStateUseCase: SaveShipmentStateUseCase,
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
                checkoutUseCase, getShipmentAddressFormUseCase,
                getShipmentAddressFormOneClickShipementUseCase,
                editAddressUseCase, changeShippingAddressUseCase,
                saveShipmentStateUseCase,
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

}