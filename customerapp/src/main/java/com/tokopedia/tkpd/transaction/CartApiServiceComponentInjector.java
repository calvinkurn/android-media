package com.tokopedia.tkpd.transaction;

import com.google.gson.Gson;
import com.tokopedia.checkout.data.repository.ICartRepository;
import com.tokopedia.checkout.domain.mapper.MapperUtil;
import com.tokopedia.checkout.domain.mapper.VoucherCouponMapper;
import com.tokopedia.checkout.domain.usecase.AddToCartUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.GetCouponListCartMarketPlaceUseCase;
import com.tokopedia.checkout.domain.usecase.GetMarketPlaceCartCounterUseCase;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 24/04/18.
 */
public class CartApiServiceComponentInjector {
    private static CartApiServiceComponentInjector instance;

    @Inject
    ICartRepository cartRepository;
    @Inject
    Gson gson;

    private AddToCartUseCase addToCartUseCase;
    private CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase;
    private CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase;
    private GetCouponListCartMarketPlaceUseCase getCouponListCartMarketPlaceUseCase;
    private GetMarketPlaceCartCounterUseCase getMarketPlaceCartCounterUseCase;

    private CartApiServiceComponent cartApiServiceComponent;

    private CartApiServiceComponentInjector(CartApiServiceComponent cartApiServiceComponent) {
        this.cartApiServiceComponent = cartApiServiceComponent;

    }

    public static CartApiServiceComponentInjector newInstance(CartApiServiceComponent cartApiServiceComponent) {
        if (instance == null) {
            instance = new CartApiServiceComponentInjector(cartApiServiceComponent).inject();
        }
        return instance;
    }

    private CartApiServiceComponentInjector inject() {
        if (cartApiServiceComponent != null) {
            cartApiServiceComponent.inject(this);
        }

        this.addToCartUseCase = new AddToCartUseCase(cartRepository, gson);
        this.checkPromoCodeCartListUseCase = new CheckPromoCodeCartListUseCase(
                cartRepository, new VoucherCouponMapper(new MapperUtil())
        );
        this.checkPromoCodeCartShipmentUseCase = new CheckPromoCodeCartShipmentUseCase(
                cartRepository, new VoucherCouponMapper(new MapperUtil())
        );
        this.getCouponListCartMarketPlaceUseCase = new GetCouponListCartMarketPlaceUseCase(
                cartRepository, new VoucherCouponMapper(new MapperUtil())
        );
        this.getMarketPlaceCartCounterUseCase = new GetMarketPlaceCartCounterUseCase(cartRepository);
        return this;
    }

    public AddToCartUseCase getAddToCartUseCase() {
        return addToCartUseCase;
    }

    public CheckPromoCodeCartListUseCase getCheckPromoCodeCartListUseCase() {
        return checkPromoCodeCartListUseCase;
    }

    public CheckPromoCodeCartShipmentUseCase getCheckPromoCodeCartShipmentUseCase() {
        return checkPromoCodeCartShipmentUseCase;
    }

    public GetCouponListCartMarketPlaceUseCase getGetCouponListCartMarketPlaceUseCase() {
        return getCouponListCartMarketPlaceUseCase;
    }

    public GetMarketPlaceCartCounterUseCase getGetMarketPlaceCartCounterUseCase() {
        return getMarketPlaceCartCounterUseCase;
    }
}
