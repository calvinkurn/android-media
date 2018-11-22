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
import com.tokopedia.checkout.domain.usecase.EditAddressUseCase;
import com.tokopedia.checkout.domain.usecase.GetCourierRecommendationUseCase;
import com.tokopedia.checkout.domain.usecase.GetRatesUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormOneClickShipementUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.checkout.domain.usecase.GetThanksToppayUseCase;
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateUseCase;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.di.module.ConverterDataModule;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.di.module.UtilModule;
import com.tokopedia.checkout.view.feature.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.feature.shipment.ShipmentContract;
import com.tokopedia.checkout.view.feature.shipment.ShipmentFragment;
import com.tokopedia.checkout.view.feature.shipment.ShipmentPresenter;
import com.tokopedia.checkout.view.feature.shipment.adapter.ShipmentAdapter;
import com.tokopedia.checkout.view.feature.shipment.converter.RatesDataConverter;
import com.tokopedia.checkout.view.feature.shipment.converter.ShipmentDataConverter;
import com.tokopedia.checkout.view.feature.shipment.converter.ShipmentDataRequestConverter;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.view.ShippingDurationConverter;
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

@Module(includes = {ConverterDataModule.class, UtilModule.class, TrackingAnalyticsModule.class})
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
                                                                       IVoucherCouponMapper voucherCouponMapper) {
        return new CheckPromoCodeCartListUseCase(cartRepository, voucherCouponMapper);
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
    ShipmentContract.Presenter provideShipmentPresenter(CompositeSubscription compositeSubscription,
                                                        CheckoutUseCase checkoutUseCase,
                                                        GetThanksToppayUseCase getThanksToppayUseCase,
                                                        CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase,
                                                        GetShipmentAddressFormUseCase getShipmentAddressFormUseCase,
                                                        GetShipmentAddressFormOneClickShipementUseCase getShipmentAddressFormOneClickShipementUseCase,
                                                        CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase,
                                                        EditAddressUseCase editAddressUseCase,
                                                        CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase,
                                                        ChangeShippingAddressUseCase changeShippingAddressUseCase,
                                                        SaveShipmentStateUseCase saveShipmentStateUseCase,
                                                        GetRatesUseCase getRatesUseCase,
                                                        GetCourierRecommendationUseCase getCourierRecommendationUseCase,
                                                        ShippingCourierConverter shippingCourierConverter,
                                                        UserSessionInterface userSessionInterface) {
        return new ShipmentPresenter(compositeSubscription, checkoutUseCase, getThanksToppayUseCase,
                checkPromoCodeCartShipmentUseCase, getShipmentAddressFormUseCase,
                getShipmentAddressFormOneClickShipementUseCase, checkPromoCodeCartListUseCase,
                editAddressUseCase, cancelAutoApplyCouponUseCase, changeShippingAddressUseCase,
                saveShipmentStateUseCase, getRatesUseCase, getCourierRecommendationUseCase,
                shippingCourierConverter, shipmentAnalyticsActionListener, userSessionInterface);
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

}
