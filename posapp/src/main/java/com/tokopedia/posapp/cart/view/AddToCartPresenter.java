package com.tokopedia.posapp.cart.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.posapp.cart.domain.model.CartDomain;
import com.tokopedia.posapp.cart.domain.usecase.AddToCartUseCase;

import javax.inject.Inject;

/**
 * Created by okasurya on 8/22/17.
 */

public class AddToCartPresenter extends BaseDaggerPresenter<AddToCart.View>
        implements AddToCart.Presenter {

    AddToCartUseCase addToCartUseCase;

    @Inject
    public AddToCartPresenter(AddToCartUseCase addToCartUseCase) {
        this.addToCartUseCase = addToCartUseCase;
    }

    @Override
    public void add(int productId, int quantity) {
        CartDomain cartDomain = new CartDomain();
        cartDomain.setProductId(productId);
        cartDomain.setQuantity(quantity);
        addToCartUseCase.execute(cartDomain, new ATCSubscriber(getView()));
    }

    public void addAndCheckout(int productId, int quantity) {
        CartDomain cartDomain = new CartDomain();
        cartDomain.setProductId(productId);
        cartDomain.setQuantity(quantity);
        addToCartUseCase.execute(cartDomain, new ATCPaymentSubscriber(getView()));
    }
}
