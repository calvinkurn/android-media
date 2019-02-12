package com.tokopedia.checkout.view.di.component;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.checkout.domain.usecase.AddToCartOneClickShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.AddToCartUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.checkout.domain.usecase.EditAddressUseCase;
import com.tokopedia.checkout.domain.usecase.GetCouponListCartMarketPlaceUseCase;
import com.tokopedia.checkout.domain.usecase.GetMarketPlaceCartCounterUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateCartUseCase;
import com.tokopedia.checkout.view.di.module.CartUseCaseModule;
import com.tokopedia.checkout.view.di.module.CheckoutRouterModule;
import com.tokopedia.checkout.view.di.module.CheckoutUseCaseModule;
import com.tokopedia.checkout.view.di.module.DataMapperModule;
import com.tokopedia.checkout.view.di.module.DataModule;
import com.tokopedia.checkout.view.di.module.DataUtilModule;
import com.tokopedia.checkout.view.di.module.ShipmentUseCaseModule;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 24/04/18.
 */
public class CartComponentInjector {
    private static CartComponentInjector instance;

    @Inject
    UpdateCartUseCase updateCartUseCase;
    @Inject
    AddToCartUseCase addToCartUseCase;
    @Inject
    AddToCartOneClickShipmentUseCase addToCartUseCaseOneClickShipment;
    @Inject
    CheckoutUseCase checkoutUseCase;
    @Inject
    CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase;
    @Inject
    CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase;
    @Inject
    GetCouponListCartMarketPlaceUseCase getCouponListCartMarketPlaceUseCase;
    @Inject
    GetMarketPlaceCartCounterUseCase getMarketPlaceCartCounterUseCase;
    @Inject
    EditAddressUseCase editAddressUseCase;
    @Inject
    UserSession userSession;

    private CartComponent cartApiServiceComponent;

    private CartComponentInjector(CartComponent cartApiServiceComponent) {
        this.cartApiServiceComponent = cartApiServiceComponent;
    }

    public CartComponent getCartApiServiceComponent() {
        return cartApiServiceComponent;
    }

    public static CartComponentInjector newInstance(Application application) {
        if (instance == null) {
            instance = new CartComponentInjector(
                    DaggerCartComponent.builder()
                            .baseAppComponent(((BaseMainApplication) application).getBaseAppComponent())
                            .checkoutRouterModule(new CheckoutRouterModule())
                            .dataMapperModule(new DataMapperModule())
                            .dataUtilModule(new DataUtilModule())
                            .dataModule(new DataModule())
                            .cartUseCaseModule(new CartUseCaseModule())
                            .checkoutUseCaseModule(new CheckoutUseCaseModule())
                            .shipmentUseCaseModule(new ShipmentUseCaseModule())
                            .build())
                    .inject();
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

    public AddToCartOneClickShipmentUseCase getAddToCartUseCaseOneClickShipment() {
        return addToCartUseCaseOneClickShipment;
    }

    public CheckoutUseCase getCheckoutUseCase() {
        return checkoutUseCase;
    }

    public UpdateCartUseCase getUpdateCartUseCase() {
        return updateCartUseCase;
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

    public UserSession getUserSession() {
        return userSession;
    }

    public EditAddressUseCase getEditAddressUseCase() {
        return editAddressUseCase;
    }
}
