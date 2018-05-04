package com.tokopedia.checkout.view.view.shipment.di;

import com.tokopedia.checkout.data.repository.ICartRepository;
import com.tokopedia.checkout.data.repository.ITopPayRepository;
import com.tokopedia.checkout.domain.mapper.CartMapper;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.checkout.domain.mapper.ICheckoutMapper;
import com.tokopedia.checkout.domain.mapper.IMapperUtil;
import com.tokopedia.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.checkout.domain.mapper.ITopPayMapper;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.checkout.domain.mapper.MapperUtil;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.checkout.domain.usecase.GetThanksToppayUseCase;
import com.tokopedia.checkout.view.di.module.ConverterDataModule;
import com.tokopedia.checkout.view.di.module.PeopleAddressModule;
import com.tokopedia.checkout.view.di.module.UtilModule;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapter;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.view.shipment.ShipmentContract;
import com.tokopedia.checkout.view.view.shipment.ShipmentPresenter;
import com.tokopedia.checkout.view.view.shipment.converter.RatesDataConverter;
import com.tokopedia.checkout.view.view.shipment.converter.ShipmentDataConverter;
import com.tokopedia.core.network.apiservices.transaction.TXActService;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Irfan Khoirul on 24/04/18.
 */

@Module(includes = {ConverterDataModule.class, PeopleAddressModule.class, UtilModule.class})
public class ShipmentModule {

    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    public ShipmentModule(ShipmentAdapterActionListener shipmentAdapterActionListener) {
        this.shipmentAdapterActionListener = shipmentAdapterActionListener;
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
    CheckoutUseCase provideCheckoutUseCase(ICartRepository cartRepository, ICheckoutMapper checkoutMapper) {
        return new CheckoutUseCase(cartRepository, checkoutMapper);
    }

    @Provides
    @ShipmentScope
    TXActService provideTXActService() {
        return new TXActService();
    }

    @Provides
    @ShipmentScope
    GetThanksToppayUseCase provideGetThanksToppayUseCase(ITopPayRepository topPayRepository, ITopPayMapper topPayMapper) {
        return new GetThanksToppayUseCase(topPayRepository, topPayMapper);
    }

    @Provides
    @ShipmentScope
    CheckPromoCodeCartShipmentUseCase provideCheckPromoCodeCartShipmentUseCase(ICartRepository cartRepository,
                                                                               IVoucherCouponMapper voucherCouponMapper) {
        return new CheckPromoCodeCartShipmentUseCase(cartRepository, voucherCouponMapper);
    }

    @Provides
    @ShipmentScope
    ShipmentDataConverter provideShipmentDataConverter() {
        return new ShipmentDataConverter();
    }

    @Provides
    @ShipmentScope
    ICartMapper provideICartMapper(IMapperUtil mapperUtil) {
        return new CartMapper(mapperUtil);
    }

    @Provides
    @ShipmentScope
    GetShipmentAddressFormUseCase provideGetShipmentAddressFormUseCase(ICartRepository cartRepository,
                                                                       IShipmentMapper shipmentMapper) {
        return new GetShipmentAddressFormUseCase(cartRepository, shipmentMapper);
    }

    @Provides
    @ShipmentScope
    ShipmentContract.Presenter provideShipmentPresenter(CompositeSubscription compositeSubscription,
                                                        CheckoutUseCase checkoutUseCase,
                                                        GetThanksToppayUseCase getThanksToppayUseCase,
                                                        CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase,
                                                        GetShipmentAddressFormUseCase getShipmentAddressFormUseCase) {
        return new ShipmentPresenter(compositeSubscription, checkoutUseCase,
                getThanksToppayUseCase, checkPromoCodeCartShipmentUseCase, getShipmentAddressFormUseCase);
    }

    @Provides
    @ShipmentScope
    ShipmentAdapter provideShipmentAdapter() {
        return new ShipmentAdapter(shipmentAdapterActionListener);
    }

    @Provides
    @ShipmentScope
    RatesDataConverter provideRatesDataConverter() {
        return new RatesDataConverter();
    }
}
