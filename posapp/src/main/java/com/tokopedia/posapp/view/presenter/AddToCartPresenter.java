package com.tokopedia.posapp.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.posapp.domain.model.cart.CartDomain;
import com.tokopedia.posapp.domain.usecase.AddToCartUseCase;
import com.tokopedia.posapp.view.AddToCart;
import com.tokopedia.posapp.view.subscriber.ATCPaymentSubscriber;
import com.tokopedia.posapp.view.subscriber.ATCSubscriber;

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
