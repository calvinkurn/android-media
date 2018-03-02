package com.tokopedia.posapp.cart.data.repository;

import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;
import com.tokopedia.posapp.cart.domain.model.CartDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 8/22/17.
 */

public interface CartRepository {
    Observable<ATCStatusDomain> storeCartProduct(CartDomain cartDomain);

    Observable<ATCStatusDomain> updateCartProduct(CartDomain cartDomain);

    Observable<ATCStatusDomain> deleteCartProduct(CartDomain cartDomain);

    Observable<ATCStatusDomain> deleteCart();

    Observable<CartDomain> getCartProduct(int productId);

    Observable<List<CartDomain>> getAllCartProducts();
}
