package com.tokopedia.checkout.view.di.component;

import com.tokopedia.checkout.domain.usecase.AddToCartUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.GetCouponListCartMarketPlaceUseCase;
import com.tokopedia.checkout.domain.usecase.GetMarketPlaceCartCounterUseCase;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 24/04/18.
 */
public class CartComponentInjector {
    private static CartComponentInjector instance;

    @Inject
    AddToCartUseCase addToCartUseCase;
    @Inject
    CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase;
    @Inject
    CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase;
    @Inject
    GetCouponListCartMarketPlaceUseCase getCouponListCartMarketPlaceUseCase;
    @Inject
    GetMarketPlaceCartCounterUseCase getMarketPlaceCartCounterUseCase;

    private CartComponent cartApiServiceComponent;

    private CartComponentInjector(CartComponent cartApiServiceComponent) {
        this.cartApiServiceComponent = cartApiServiceComponent;
    }

    public CartComponent getCartApiServiceComponent() {
        return cartApiServiceComponent;
    }

    public static CartComponentInjector newInstance(CartComponent cartApiServiceComponent) {
        if (instance == null) {
            instance = new CartComponentInjector(cartApiServiceComponent).inject();
        }
        return instance;
    }

    private CartComponentInjector inject() {
        if (cartApiServiceComponent != null) {
            cartApiServiceComponent.inject(this);
        }
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
