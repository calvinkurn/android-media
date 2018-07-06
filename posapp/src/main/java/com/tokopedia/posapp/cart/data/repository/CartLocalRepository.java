package com.tokopedia.posapp.cart.data.repository;


import com.tokopedia.posapp.cart.CartConstant;
import com.tokopedia.posapp.cart.data.source.CartLocalSource;
import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;
import com.tokopedia.posapp.cart.domain.model.CartDomain;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 8/22/17.
 */

public class CartLocalRepository implements CartRepository {
    private CartLocalSource cartLocalSource;

    @Inject
    public CartLocalRepository(CartLocalSource cartLocalSource) {
        this.cartLocalSource = cartLocalSource;
    }

    @Override
    public Observable<ATCStatusDomain> storeCartProduct(RequestParams requestParams) {
        CartDomain cart = (CartDomain) requestParams.getObject(CartConstant.KEY_CART);
        return cartLocalSource.storeCartProduct(cart);
    }

    @Override
    public Observable<ATCStatusDomain> updateCartProduct(RequestParams requestParams) {
        CartDomain cart = (CartDomain) requestParams.getObject(CartConstant.KEY_CART);
        CartDomain existingCart = (CartDomain) requestParams.getObject(CartConstant.KEY_EXISTING_CART);
        cart.setQuantity(existingCart.getQuantity() + cart.getQuantity());
        return cartLocalSource.updateCartProduct(cart);
    }

    @Override
    public Observable<ATCStatusDomain> deleteCartProduct(CartDomain cartDomain) {
        return cartLocalSource.deleteCartProduct(cartDomain);
    }

    @Override
    public Observable<ATCStatusDomain> deleteCart() {
        return cartLocalSource.deleteCart();
    }

    @Override
    public Observable<CartDomain> getCartProduct(RequestParams requestParams) {
        CartDomain cart = (CartDomain) requestParams.getObject(CartConstant.KEY_CART);
        return cartLocalSource.getCartProduct(cart.getProductId());
    }

    @Override
    public Observable<List<CartDomain>> getAllCartProducts() {
        return cartLocalSource.getAllCartProducts();
    }
}
