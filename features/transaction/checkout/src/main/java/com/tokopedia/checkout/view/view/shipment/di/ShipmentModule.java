package com.tokopedia.checkout.view.view.shipment.di;

import com.tokopedia.checkout.data.repository.ICartRepository;
import com.tokopedia.checkout.data.repository.ITopPayRepository;
import com.tokopedia.checkout.data.repository.TopPayRepository;
import com.tokopedia.checkout.domain.mapper.CartMapper;
import com.tokopedia.checkout.domain.mapper.CheckoutMapper;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.checkout.domain.mapper.ICheckoutMapper;
import com.tokopedia.checkout.domain.mapper.IMapperUtil;
import com.tokopedia.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.checkout.domain.mapper.ITopPayMapper;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.checkout.domain.mapper.ShipmentMapper;
import com.tokopedia.checkout.domain.mapper.TopPayMapper;
import com.tokopedia.checkout.domain.mapper.VoucherCouponMapper;
import com.tokopedia.checkout.domain.usecase.CartListInteractor;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.checkout.domain.usecase.GetThanksToppayUseCase;
import com.tokopedia.checkout.domain.usecase.ICartListInteractor;
import com.tokopedia.checkout.view.di.module.DataModule;
import com.tokopedia.checkout.view.di.module.UtilModule;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapter;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapterActionListener;
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

@Module(includes = {DataModule.class, UtilModule.class})
public class ShipmentModule {

    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    public ShipmentModule(ShipmentAdapterActionListener shipmentAdapterActionListener) {
        this.shipmentAdapterActionListener = shipmentAdapterActionListener;
    }

    // Inject CartListInteractor
//    @Provides
//    @ShipmentScope

    // Inject composite subscription
    @Provides
    @ShipmentScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @ShipmentScope
    ICheckoutMapper provideICheckoutMapper(IMapperUtil mapperUtil) {
        return new CheckoutMapper(mapperUtil);
    }

    // Inject CheckoutUseCase
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
    ITopPayRepository provideITopPayRepository(TXActService txActService) {
        return new TopPayRepository(txActService);
    }

    @Provides
    @ShipmentScope
    ITopPayMapper provideITopPayMapper() {
        return new TopPayMapper();
    }

    // Inject GetThanksToppayUseCase
    @Provides
    @ShipmentScope
    GetThanksToppayUseCase provideGetThanksToppayUseCase(ITopPayRepository topPayRepository, ITopPayMapper topPayMapper) {
        return new GetThanksToppayUseCase(topPayRepository, topPayMapper);
    }

    @Provides
    @ShipmentScope
    IVoucherCouponMapper provideIVoucherCouponMapper(IMapperUtil mapperUtil) {
        return new VoucherCouponMapper(mapperUtil);
    }

    // Inject CheckPromoCodeCartShipmentUseCase
    @Provides
    @ShipmentScope
    CheckPromoCodeCartShipmentUseCase provideCheckPromoCodeCartShipmentUseCase(ICartRepository cartRepository,
                                                                               IVoucherCouponMapper voucherCouponMapper) {
        return new CheckPromoCodeCartShipmentUseCase(cartRepository, voucherCouponMapper);
    }

    // Inject ShipmentDataConverter
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
    IShipmentMapper provideIShipmentMapper(IMapperUtil mapperUtil) {
        return new ShipmentMapper(mapperUtil);
    }

    @Provides
    @ShipmentScope
    ICartListInteractor provideICartListInteractor(CompositeSubscription compositeSubscription,
                                                   ICartRepository cartRepository,
                                                   ICartMapper cartMapper,
                                                   IShipmentMapper shipmentMapper,
                                                   IVoucherCouponMapper voucherCouponMapper) {
        return new CartListInteractor(compositeSubscription, cartRepository, cartMapper, shipmentMapper, voucherCouponMapper);
    }

    // Inject presenter
    @Provides
    @ShipmentScope
    ShipmentPresenter provideShipmentPresenter(ICartListInteractor iCartListInteractor,
                                               CompositeSubscription compositeSubscription,
                                               CheckoutUseCase checkoutUseCase,
                                               GetThanksToppayUseCase getThanksToppayUseCase,
                                               CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase) {
        return new ShipmentPresenter(iCartListInteractor, compositeSubscription, checkoutUseCase,
                getThanksToppayUseCase, checkPromoCodeCartShipmentUseCase);
    }

    // Inject Adapter
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
