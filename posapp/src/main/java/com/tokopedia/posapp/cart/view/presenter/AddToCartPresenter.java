package com.tokopedia.posapp.cart.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.posapp.cart.CartConstant;
import com.tokopedia.posapp.cart.domain.model.CartDomain;
import com.tokopedia.posapp.cart.domain.usecase.AddToCartUseCase;
import com.tokopedia.posapp.cart.view.subscriber.ATCPaymentSubscriber;
import com.tokopedia.posapp.cart.view.subscriber.ATCSubscriber;
import com.tokopedia.posapp.cart.view.AddToCart;
import com.tokopedia.usecase.RequestParams;

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
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CartConstant.KEY_CART, cartDomain);
        addToCartUseCase.execute(requestParams, new ATCSubscriber(getView()));
    }

    public void addAndCheckout(int productId, int quantity) {
        CartDomain cartDomain = new CartDomain();
        cartDomain.setProductId(productId);
        cartDomain.setQuantity(quantity);
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CartConstant.KEY_CART, cartDomain);
        addToCartUseCase.execute(requestParams, new ATCPaymentSubscriber(getView()));
    }
}
