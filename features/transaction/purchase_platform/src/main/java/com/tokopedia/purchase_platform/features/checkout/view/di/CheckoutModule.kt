package com.tokopedia.purchase_platform.features.checkout.view.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.usecase.GetCourierRecommendationUseCase
import com.tokopedia.logisticdata.data.analytics.CodAnalytics
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeFinalUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.base.IMapperUtil
import com.tokopedia.purchase_platform.common.di2.*
import com.tokopedia.purchase_platform.common.feature.promo.PromoActionListener
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
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import rx.subscriptions.CompositeSubscription

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
    fun provideIShipmentMapper(iMapperUtil: IMapperUtil): IShipmentMapper {
        return ShipmentMapper(iMapperUtil)
    }

    @Provides
    @CheckoutScope
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources)
    }

    @Provides
    @CheckoutScope
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }

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
                                 getCourierRecommendationUseCase: GetCourierRecommendationUseCase,
                                 clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
                                 shippingCourierConverter: ShippingCourierConverter,
                                 userSessionInterface: UserSessionInterface,
                                 analyticsPurchaseProtection: CheckoutAnalyticsPurchaseProtection,
                                 codAnalytics: CodAnalytics,
                                 checkoutAnalytics: CheckoutAnalyticsCourierSelection): ShipmentContract.Presenter {
        return ShipmentPresenter(checkPromoStackingCodeFinalUseCase,
                checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, compositeSubscription,
                checkoutUseCase, getShipmentAddressFormUseCase,
                getShipmentAddressFormOneClickShipementUseCase,
                editAddressUseCase, changeShippingAddressUseCase,
                saveShipmentStateUseCase, getCourierRecommendationUseCase,
                codCheckoutUseCase, clearCacheAutoApplyStackUseCase, shippingCourierConverter,
                shipmentFragment, userSessionInterface,
                analyticsPurchaseProtection, codAnalytics, checkoutAnalytics)
    }

    @Provides
    @CheckoutScope
    fun provideShipmentAdapterActionListener(): ShipmentAdapterActionListener {
        return shipmentFragment
    }

    @Provides
    @CheckoutScope
    fun providePromoActionListener(): PromoActionListener {
        return shipmentFragment
    }

    @Provides
    @CheckoutScope
    fun provideTrackingPromo(): TrackingPromoCheckoutUtil {
        return TrackingPromoCheckoutUtil()
    }

}