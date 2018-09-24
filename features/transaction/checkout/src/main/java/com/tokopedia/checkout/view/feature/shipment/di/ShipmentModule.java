package com.tokopedia.checkout.view.feature.shipment.di;

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
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.checkout.domain.usecase.GetThanksToppayUseCase;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.di.module.ConverterDataModule;
import com.tokopedia.checkout.view.di.module.PeopleAddressModule;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.di.module.UtilModule;
import com.tokopedia.checkout.view.feature.shipment.adapter.ShipmentAdapter;
import com.tokopedia.checkout.view.feature.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.feature.shipment.ShipmentContract;
import com.tokopedia.checkout.view.feature.shipment.ShipmentFragment;
import com.tokopedia.checkout.view.feature.shipment.ShipmentPresenter;
import com.tokopedia.checkout.view.feature.shipment.converter.RatesDataConverter;
import com.tokopedia.checkout.view.feature.shipment.converter.ShipmentDataConverter;
import com.tokopedia.checkout.view.feature.shipment.converter.ShipmentDataRequestConverter;
import com.tokopedia.core.network.apiservices.transaction.TXActService;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.transactiondata.repository.ITopPayRepository;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Irfan Khoirul on 24/04/18.
 */

@Module(includes = {ConverterDataModule.class, PeopleAddressModule.class, UtilModule.class, TrackingAnalyticsModule.class})
public class ShipmentModule {

    private ShipmentAdapterActionListener shipmentAdapterActionListener;
    private ShipmentContract.AnalyticsActionListener shipmentAnalyticsActionListener;

    public ShipmentModule(ShipmentFragment shipmentFragment) {
        this.shipmentAdapterActionListener = shipmentFragment;
        this.shipmentAnalyticsActionListener = shipmentFragment;
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
    TXActService provideTXActService() {
        return new TXActService();
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
    CheckPromoCodeCartListUseCase provideCheckPromoCodeCartListUseCase(ICartRepository cartRepository,
                                                                       IVoucherCouponMapper voucherCouponMapper) {
        return new CheckPromoCodeCartListUseCase(cartRepository, voucherCouponMapper);
    }

    @Provides
    @ShipmentScope
    ShipmentContract.Presenter provideShipmentPresenter(CompositeSubscription compositeSubscription,
                                                        CheckoutUseCase checkoutUseCase,
                                                        GetThanksToppayUseCase getThanksToppayUseCase,
                                                        CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase,
                                                        GetShipmentAddressFormUseCase getShipmentAddressFormUseCase,
                                                        CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase,
                                                        EditAddressUseCase editAddressUseCase,
                                                        CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase,
                                                        ChangeShippingAddressUseCase changeShippingAddressUseCase) {
        return new ShipmentPresenter(compositeSubscription, checkoutUseCase, getThanksToppayUseCase,
                checkPromoCodeCartShipmentUseCase, getShipmentAddressFormUseCase,
                checkPromoCodeCartListUseCase, editAddressUseCase, cancelAutoApplyCouponUseCase,
                changeShippingAddressUseCase, shipmentAnalyticsActionListener);
    }

    @Provides
    @ShipmentScope
    ShipmentDataRequestConverter provideShipmentDataRequestConverter() {
        return new ShipmentDataRequestConverter();
    }

    @Provides
    @ShipmentScope
    ShipmentAdapter provideShipmentAdapter(ShipmentDataRequestConverter shipmentDataRequestConverter) {
        return new ShipmentAdapter(shipmentAdapterActionListener, shipmentDataRequestConverter);
    }

    @Provides
    @ShipmentScope
    RatesDataConverter provideRatesDataConverter() {
        return new RatesDataConverter();
    }
}
