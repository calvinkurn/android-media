package com.tokopedia.purchase_platform.checkout.view.feature.shipment.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.purchase_platform.cart.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.purchase_platform.checkout.data.AddressRepository;
import com.tokopedia.purchase_platform.checkout.domain.mapper.ICheckoutMapper;
import com.tokopedia.purchase_platform.common.base.IMapperUtil;
import com.tokopedia.purchase_platform.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.purchase_platform.common.base.MapperUtil;
import com.tokopedia.purchase_platform.common.feature.promo.domain.CancelAutoApplyCouponUseCase;
import com.tokopedia.purchase_platform.checkout.domain.usecase.ChangeShippingAddressUseCase;
import com.tokopedia.purchase_platform.cart.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.purchase_platform.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.purchase_platform.checkout.domain.usecase.CodCheckoutUseCase;
import com.tokopedia.purchase_platform.checkout.domain.usecase.EditAddressUseCase;
import com.tokopedia.purchase_platform.checkout.domain.usecase.GetShipmentAddressFormOneClickShipementUseCase;
import com.tokopedia.purchase_platform.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.purchase_platform.checkout.domain.usecase.SaveShipmentStateUseCase;
import com.tokopedia.purchase_platform.common.router.ICheckoutModuleRouter;
import com.tokopedia.purchase_platform.common.feature.promo.PromoActionListener;
import com.tokopedia.purchase_platform.common.di.module.ConverterDataModule;
import com.tokopedia.purchase_platform.common.di.module.TrackingAnalyticsModule;
import com.tokopedia.purchase_platform.common.di.module.UtilModule;
import com.tokopedia.purchase_platform.checkout.view.feature.shipment.adapter.ShipmentAdapter;
import com.tokopedia.purchase_platform.checkout.view.feature.shipment.ShipmentAdapterActionListener;
import com.tokopedia.purchase_platform.checkout.view.feature.shipment.ShipmentContract;
import com.tokopedia.purchase_platform.checkout.view.feature.shipment.ShipmentFragment;
import com.tokopedia.purchase_platform.checkout.view.feature.shipment.ShipmentPresenter;
import com.tokopedia.purchase_platform.checkout.view.feature.shipment.converter.RatesDataConverter;
import com.tokopedia.purchase_platform.checkout.view.feature.shipment.converter.ShipmentDataConverter;
import com.tokopedia.purchase_platform.checkout.view.feature.shipment.converter.ShipmentDataRequestConverter;
import com.tokopedia.logisticdata.data.analytics.CodAnalytics;
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeFinalUseCase;
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase;
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase;
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper;
import com.tokopedia.shipping_recommendation.domain.usecase.GetCourierRecommendationUseCase;
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationConverter;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil;
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule;
import com.tokopedia.promocheckout.common.di.PromoCheckoutQualifier;
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeUseCase;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsPurchaseProtection;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Irfan Khoirul on 24/04/18.
 */

@Module(includes = {ConverterDataModule.class, UtilModule.class, TrackingAnalyticsModule.class, PromoCheckoutModule.class})
public class ShipmentModule {

    private ShipmentAdapterActionListener shipmentAdapterActionListener;
    private PromoActionListener promoActionListener;
    private ShipmentContract.AnalyticsActionListener shipmentAnalyticsActionListener;
    private ShipmentContract.View view;

    public ShipmentModule(ShipmentFragment shipmentFragment) {
        this.shipmentAdapterActionListener = shipmentFragment;
        this.promoActionListener = shipmentFragment;
        this.shipmentAnalyticsActionListener = shipmentFragment;
        this.view = shipmentFragment;
    }

    @Provides
    @ShipmentScope
    IMapperUtil provideIMapperUtil() {
        return new MapperUtil();
    }

    @Provides
    @ShipmentScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @ShipmentScope
    CheckoutUseCase provideCheckoutUseCase(
            ICheckoutModuleRouter checkoutModuleRouter,
            ICartRepository cartRepository,
            ICheckoutMapper checkoutMapper) {
        return new CheckoutUseCase(cartRepository, checkoutMapper, checkoutModuleRouter);
    }

    @Provides
    @ShipmentScope
    CodCheckoutUseCase provideCodCheckoutUseCase(Context context, ICheckoutModuleRouter checkoutModuleRouter) {
        return new CodCheckoutUseCase(context, checkoutModuleRouter);
    }

    @Provides
    @ShipmentScope
    EditAddressUseCase provideEditAddressUseCase(AddressRepository addressRepository) {
        return new EditAddressUseCase(addressRepository);
    }

    @Provides
    @ShipmentScope
    ShipmentDataConverter provideShipmentDataConverter() {
        return new ShipmentDataConverter();
    }

    @Provides
    @ShipmentScope
    GetShipmentAddressFormUseCase provideGetShipmentAddressFormUseCase(ICartRepository cartRepository,
                                                                       IShipmentMapper shipmentMapper) {
        return new GetShipmentAddressFormUseCase(cartRepository, shipmentMapper);
    }

    @Provides
    @ShipmentScope
    GetShipmentAddressFormOneClickShipementUseCase provideGetShipmentAddressFormOneClickShipmentUseCase(
            ICartRepository cartRepository, IShipmentMapper shipmentMapper) {
        return new GetShipmentAddressFormOneClickShipementUseCase(cartRepository, shipmentMapper);
    }

    @Provides
    @ShipmentScope
    CheckPromoCodeCartListUseCase provideCheckPromoCodeCartListUseCase(ICartRepository cartRepository,
                                                                       IVoucherCouponMapper voucherCouponMapper,
                                                                       @PromoCheckoutQualifier CheckPromoCodeUseCase checkPromoCodeUseCase) {
        return new CheckPromoCodeCartListUseCase(cartRepository, voucherCouponMapper, checkPromoCodeUseCase);
    }

    @Provides
    @ShipmentScope
    SaveShipmentStateUseCase provideSaveShipmentStateUseCase(ICartRepository iCartRepository) {
        return new SaveShipmentStateUseCase(iCartRepository);
    }

    @Provides
    @ShipmentScope
    ShippingDurationConverter provideShippingDurationConverter() {
        return new ShippingDurationConverter();
    }

    @Provides
    @ShipmentScope
    GetCourierRecommendationUseCase provideGetCourierRecommendationUseCase(ShippingDurationConverter shippingDurationConverter) {
        return new GetCourierRecommendationUseCase(shippingDurationConverter);
    }

    @Provides
    @ShipmentScope
    ShippingCourierConverter provideShippingCourierConverter() {
        return new ShippingCourierConverter();
    }

    @Provides
    @ShipmentScope
    UserSessionInterface provideUserSessionInterface() {
        return new UserSession(view.getActivityContext());
    }

    @Provides
    @ShipmentScope
    CheckPromoStackingCodeUseCase provideCheckPromoStackingCodeUseCase(@ApplicationContext Context context) {
        return new CheckPromoStackingCodeUseCase(context.getResources());
    }

    @Provides
    @ShipmentScope
    ShipmentContract.Presenter provideShipmentPresenter(@PromoCheckoutQualifier CheckPromoStackingCodeFinalUseCase checkPromoStackingCodeFinalUseCase,
                                                        CheckPromoStackingCodeUseCase checkPromoStackingCodeUseCase,
                                                        CheckPromoStackingCodeMapper checkPromoStackingCodeMapper,
                                                        CompositeSubscription compositeSubscription,
                                                        CheckoutUseCase checkoutUseCase,
                                                        GetShipmentAddressFormUseCase getShipmentAddressFormUseCase,
                                                        GetShipmentAddressFormOneClickShipementUseCase getShipmentAddressFormOneClickShipementUseCase,
                                                        EditAddressUseCase editAddressUseCase,
                                                        CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase,
                                                        ChangeShippingAddressUseCase changeShippingAddressUseCase,
                                                        SaveShipmentStateUseCase saveShipmentStateUseCase,
                                                        CodCheckoutUseCase codCheckoutUseCase,
                                                        GetCourierRecommendationUseCase getCourierRecommendationUseCase,
                                                        ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase,
                                                        ShippingCourierConverter shippingCourierConverter,
                                                        UserSessionInterface userSessionInterface,
                                                        CheckoutAnalyticsPurchaseProtection analyticsPurchaseProtection,
                                                        CodAnalytics codAnalytics,
                                                        CheckoutAnalyticsCourierSelection checkoutAnalytics) {
        return new ShipmentPresenter(checkPromoStackingCodeFinalUseCase,
                checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, compositeSubscription,
                checkoutUseCase, getShipmentAddressFormUseCase,
                getShipmentAddressFormOneClickShipementUseCase,
                editAddressUseCase, cancelAutoApplyCouponUseCase, changeShippingAddressUseCase,
                saveShipmentStateUseCase, getCourierRecommendationUseCase,
                codCheckoutUseCase, clearCacheAutoApplyStackUseCase, shippingCourierConverter,
                shipmentAnalyticsActionListener, userSessionInterface,
                analyticsPurchaseProtection, codAnalytics, checkoutAnalytics);
    }

    @Provides
    @ShipmentScope
    ShipmentDataRequestConverter provideShipmentDataRequestConverter() {
        return new ShipmentDataRequestConverter();
    }

    @Provides
    @ShipmentScope
    RatesDataConverter provideRatesDataConverter() {
        return new RatesDataConverter();
    }

    @Provides
    @ShipmentScope
    ShipmentAdapter provideShipmentAdapter(ShipmentDataRequestConverter shipmentDataRequestConverter,
                                           RatesDataConverter ratesDataConverter) {
        return new ShipmentAdapter(shipmentAdapterActionListener, promoActionListener, shipmentDataRequestConverter, ratesDataConverter);
    }

    @Provides
    @ShipmentScope
    @ApplicationContext
    Context provideContextAbstraction(Context context) {
        return context;
    }

    @Provides
    @ShipmentScope
    TrackingPromoCheckoutUtil provideTrackingPromo(@ApplicationContext Context context) {
        return new TrackingPromoCheckoutUtil();
    }
}
