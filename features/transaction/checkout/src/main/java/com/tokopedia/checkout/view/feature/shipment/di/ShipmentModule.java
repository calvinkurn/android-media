package com.tokopedia.checkout.view.feature.shipment.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.checkout.data.repository.AddressRepository;
import com.tokopedia.checkout.domain.mapper.ICheckoutMapper;
import com.tokopedia.checkout.domain.mapper.IMapperUtil;
import com.tokopedia.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.checkout.domain.mapper.ITopPayMapper;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.checkout.domain.mapper.MapperUtil;
import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.checkout.domain.usecase.CodCheckoutUseCase;
import com.tokopedia.checkout.domain.usecase.EditAddressUseCase;
import com.tokopedia.checkout.domain.usecase.GetRatesUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormOneClickShipementUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.checkout.domain.usecase.GetThanksToppayUseCase;
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateUseCase;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.di.module.ConverterDataModule;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.di.module.UtilModule;
import com.tokopedia.checkout.view.di.scope.CartListScope;
import com.tokopedia.checkout.view.feature.shipment.adapter.ShipmentAdapter;
import com.tokopedia.checkout.view.feature.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.feature.shipment.ShipmentContract;
import com.tokopedia.checkout.view.feature.shipment.ShipmentFragment;
import com.tokopedia.checkout.view.feature.shipment.ShipmentPresenter;
import com.tokopedia.checkout.view.feature.shipment.adapter.ShipmentAdapter;
import com.tokopedia.checkout.view.feature.shipment.converter.RatesDataConverter;
import com.tokopedia.checkout.view.feature.shipment.converter.ShipmentDataConverter;
import com.tokopedia.checkout.view.feature.shipment.converter.ShipmentDataRequestConverter;
import com.tokopedia.logisticanalytics.CodAnalytics;
import com.tokopedia.shipping_recommendation.domain.usecase.GetCourierRecommendationUseCase;
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationConverter;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutRouter;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil;
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule;
import com.tokopedia.promocheckout.common.di.PromoCheckoutQualifier;
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeFinalUseCase;
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeUseCase;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsPurchaseProtection;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.transactiondata.repository.ITopPayRepository;
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
    private ShipmentContract.AnalyticsActionListener shipmentAnalyticsActionListener;
    private ShipmentContract.View view;

    public ShipmentModule(ShipmentFragment shipmentFragment) {
        this.shipmentAdapterActionListener = shipmentFragment;
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
    GetThanksToppayUseCase provideGetThanksToppayUseCase(ITopPayRepository topPayRepository,
                                                         ITopPayMapper topPayMapper) {
        return new GetThanksToppayUseCase(topPayRepository, topPayMapper);
    }

    @Provides
    @ShipmentScope
    EditAddressUseCase provideEditAddressUseCase(AddressRepository addressRepository) {
        return new EditAddressUseCase(addressRepository);
    }

    @Provides
    @ShipmentScope
    CheckPromoCodeCartShipmentUseCase provideCheckPromoCodeCartShipmentUseCase(
            ICartRepository cartRepository, IVoucherCouponMapper voucherCouponMapper) {
        return new CheckPromoCodeCartShipmentUseCase(cartRepository, voucherCouponMapper);
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
    ShipmentContract.Presenter provideShipmentPresenter(@PromoCheckoutQualifier CheckPromoCodeFinalUseCase checkPromoCodeFinalUseCase,
                                                        CompositeSubscription compositeSubscription,
                                                        CheckoutUseCase checkoutUseCase,
                                                        GetThanksToppayUseCase getThanksToppayUseCase,
                                                        GetShipmentAddressFormUseCase getShipmentAddressFormUseCase,
                                                        GetShipmentAddressFormOneClickShipementUseCase getShipmentAddressFormOneClickShipementUseCase,
                                                        CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase,
                                                        EditAddressUseCase editAddressUseCase,
                                                        CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase,
                                                        ChangeShippingAddressUseCase changeShippingAddressUseCase,
                                                        SaveShipmentStateUseCase saveShipmentStateUseCase,
                                                        GetRatesUseCase getRatesUseCase,
                                                        CodCheckoutUseCase codCheckoutUseCase,
                                                        GetCourierRecommendationUseCase getCourierRecommendationUseCase,
                                                        ShippingCourierConverter shippingCourierConverter,
                                                        UserSessionInterface userSessionInterface,
                                                        IVoucherCouponMapper voucherCouponMapper,
                                                        CheckoutAnalyticsPurchaseProtection analyticsPurchaseProtection,
        CodAnalytics codAnalytics) {return new ShipmentPresenter(checkPromoCodeFinalUseCase,
                compositeSubscription, checkoutUseCase, getThanksToppayUseCase, getShipmentAddressFormUseCase,getShipmentAddressFormOneClickShipementUseCase,
                 checkPromoCodeCartListUseCase,
                editAddressUseCase, cancelAutoApplyCouponUseCase, changeShippingAddressUseCase,
                saveShipmentStateUseCase, getRatesUseCase, getCourierRecommendationUseCase,
               codCheckoutUseCase, shippingCourierConverter,  shipmentAnalyticsActionListener, voucherCouponMapper, userSessionInterface,analyticsPurchaseProtection, codAnalytics);
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
        return new ShipmentAdapter(shipmentAdapterActionListener, shipmentDataRequestConverter, ratesDataConverter);
    }

    @Provides
    @ShipmentScope
    @ApplicationContext
    Context provideContextAbstraction(Context context){
        return context;
    }

    @Provides
    @ShipmentScope
    TrackingPromoCheckoutUtil provideTrackingPromo(@ApplicationContext Context context) {
        if(context instanceof TrackingPromoCheckoutRouter){
            return new TrackingPromoCheckoutUtil((TrackingPromoCheckoutRouter)context);
        }else{
            return new TrackingPromoCheckoutUtil(null);
        }
    }
}
