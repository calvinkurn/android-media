package com.tokopedia.checkout.view.di.module;

import com.google.gson.Gson;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.checkout.domain.usecase.AddToCartUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartUpdateCartUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetCouponListCartMarketPlaceUseCase;
import com.tokopedia.checkout.domain.usecase.GetMarketPlaceCartCounterUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartGetShipmentFormUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateCartGetShipmentAddressFormUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 25/04/18.
 */
@Module
public class CartUseCaseModule {

    @Provides
    AddToCartUseCase addToCartUseCase(ICartRepository cartRepository, Gson gson) {
        return new AddToCartUseCase(cartRepository, gson);
    }

    @Provides
    CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase(ICartRepository cartRepository,
                                                                IVoucherCouponMapper iVoucherCouponMapper) {
        return new CheckPromoCodeCartListUseCase(cartRepository, iVoucherCouponMapper);
    }

    @Provides
    CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase(ICartRepository cartRepository,
                                                                        IVoucherCouponMapper voucherCouponMapper) {
        return new CheckPromoCodeCartShipmentUseCase(cartRepository, voucherCouponMapper);
    }

    @Provides
    GetCouponListCartMarketPlaceUseCase getCouponListCartMarketPlaceUseCase(ICartRepository cartRepository,
                                                                            IVoucherCouponMapper voucherCouponMapper) {
        return new GetCouponListCartMarketPlaceUseCase(cartRepository, voucherCouponMapper);
    }

    @Provides
    GetMarketPlaceCartCounterUseCase getMarketPlaceCartCounterUseCase(ICartRepository cartRepository) {
        return new GetMarketPlaceCartCounterUseCase(cartRepository);
    }

    @Provides
    GetCartListUseCase getCartListUseCase(ICartRepository cartRepository, ICartMapper mapper) {
        return new GetCartListUseCase(cartRepository, mapper);
    }

    @Provides
    DeleteCartUseCase deleteCartUseCase(ICartRepository cartRepository, ICartMapper mapper) {
        return new DeleteCartUseCase(cartRepository, mapper);
    }

    @Provides
    DeleteCartUpdateCartUseCase deleteCartUpdateCartUseCase(ICartRepository cartRepository, ICartMapper mapper) {
        return new DeleteCartUpdateCartUseCase(cartRepository, mapper);
    }

    @Provides
    DeleteCartGetCartListUseCase deleteCartGetCartListUseCase(ICartRepository cartRepository, ICartMapper mapper) {
        return new DeleteCartGetCartListUseCase(cartRepository, mapper);
    }

    @Provides
    UpdateCartGetShipmentAddressFormUseCase updateCartGetShipmentAddressFormUseCase(
            ICartRepository cartRepository, ICartMapper cartMapper, IShipmentMapper shipmentMapper
    ) {
        return new UpdateCartGetShipmentAddressFormUseCase(cartRepository, cartMapper, shipmentMapper);
    }

    @Provides
    GetShipmentAddressFormUseCase getShipmentAddressFormUseCase(
            ICartRepository cartRepository, IShipmentMapper shipmentMapper
    ) {
        return new GetShipmentAddressFormUseCase(cartRepository, shipmentMapper);
    }

    @Provides
    ResetCartGetCartListUseCase resetCartGetCartListUseCase(
            ICartRepository cartRepository, ICartMapper cartMapper
    ) {
        return new ResetCartGetCartListUseCase(cartRepository, cartMapper);
    }

    @Provides
    ResetCartGetShipmentFormUseCase resetCartGetShipmentFormUseCase(
            ICartRepository cartRepository, ICartMapper cartMapper, IShipmentMapper shipmentMapper
    ) {
        return new ResetCartGetShipmentFormUseCase(cartRepository, cartMapper, shipmentMapper);
    }
}
