package com.tokopedia.posapp.cart.data.repository;


import com.tokopedia.posapp.cart.data.source.CartLocalSource;
import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;
import com.tokopedia.posapp.cart.domain.model.CartDomain;

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
    public Observable<ATCStatusDomain> storeCartProduct(CartDomain cartDomain) {
        return cartLocalSource.storeCartProduct(cartDomain);
    }

    @Override
    public Observable<ATCStatusDomain> updateCartProduct(CartDomain cartDomain) {
        return cartLocalSource.updateCartProduct(cartDomain);
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
    public Observable<CartDomain> getCartProduct(int productId) {
        return cartLocalSource.getCartProduct(productId);
    }

    @Override
    public Observable<List<CartDomain>> getAllCartProducts() {
        return cartLocalSource.getAllCartProducts();
    }
}
