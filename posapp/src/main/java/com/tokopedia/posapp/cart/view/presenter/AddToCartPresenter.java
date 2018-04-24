package com.tokopedia.posapp.cart.view.presenter;

import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.cart.CartConstant;
import com.tokopedia.posapp.cart.domain.model.CartDomain;
import com.tokopedia.posapp.cart.domain.usecase.AddToCartUseCase;
import com.tokopedia.posapp.cart.view.AddToCart;
import com.tokopedia.posapp.cart.view.subscriber.ATCPaymentSubscriber;
import com.tokopedia.posapp.cart.view.subscriber.ATCSubscriber;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

/**
 * Created by okasurya on 8/22/17.
 */

public class AddToCartPresenter implements AddToCart.Presenter {

    private AddToCartUseCase addToCartUseCase;
    private AddToCart.View view;

    @Inject
    public AddToCartPresenter(AddToCartUseCase addToCartUseCase) {
        this.addToCartUseCase = addToCartUseCase;
    }

    @Override
    public void add(ProductDetailData product, int quantity) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CartConstant.KEY_CART, getCart(product, quantity));
        addToCartUseCase.execute(requestParams, new ATCSubscriber(this.view));
    }

    public void addAndCheckout(ProductDetailData product, int quantity) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CartConstant.KEY_CART, getCart(product, quantity));
        addToCartUseCase.execute(requestParams, new ATCPaymentSubscriber(this.view));
    }

    private CartDomain getCart(ProductDetailData product, int quantity) {
        CartDomain cartDomain = new CartDomain();
        cartDomain.setProductId(product.getInfo().getProductId());
        cartDomain.setProductId(product.getInfo().getProductId());
        cartDomain.setProductName(product.getInfo().getProductName());
        if(product.getProductImages() != null && product.getProductImages().size() > 0) {
            cartDomain.setProductImage300(product.getProductImages().get(0).getImageSrc300());
        }
        cartDomain.setProductPrice(product.getInfo().getProductPrice());
        cartDomain.setProductPriceUnformatted(product.getInfo().getProductPriceUnformatted());
        cartDomain.setQuantity(quantity);
        return cartDomain;
    }

    @Override
    public void attachView(AddToCart.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
