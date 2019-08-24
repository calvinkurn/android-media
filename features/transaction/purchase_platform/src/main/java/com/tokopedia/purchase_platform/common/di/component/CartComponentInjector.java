package com.tokopedia.purchase_platform.common.di.component;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.EditAddressUseCase;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.UpdateCartUseCase;
import com.tokopedia.purchase_platform.common.di.module.CartUseCaseModule;
import com.tokopedia.purchase_platform.common.di.module.CheckoutRouterModule;
import com.tokopedia.purchase_platform.common.di.module.CheckoutUseCaseModule;
import com.tokopedia.purchase_platform.common.di.module.DataMapperModule;
import com.tokopedia.purchase_platform.common.di.module.DataModule;
import com.tokopedia.purchase_platform.common.di.module.DataUtilModule;
import com.tokopedia.purchase_platform.common.di.module.ShipmentUseCaseModule;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 24/04/18.
 */
public class CartComponentInjector {
    private static CartComponentInjector instance;

//    @Inject
    UpdateCartUseCase updateCartUseCase;
//    @Inject
    CheckoutUseCase checkoutUseCase;
//    @Inject
    CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase;
//    @Inject
    EditAddressUseCase editAddressUseCase;
//    @Inject
    UserSessionInterface userSession;

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

    public CheckoutUseCase getCheckoutUseCase() {
        return checkoutUseCase;
    }

    public UpdateCartUseCase getUpdateCartUseCase() {
        return updateCartUseCase;
    }

    public CheckPromoCodeCartListUseCase getCheckPromoCodeCartListUseCase() {
        return checkPromoCodeCartListUseCase;
    }

    public UserSessionInterface getUserSession() {
        return userSession;
    }

    public EditAddressUseCase getEditAddressUseCase() {
        return editAddressUseCase;
    }
}
