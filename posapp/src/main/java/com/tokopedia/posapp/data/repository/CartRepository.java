package com.tokopedia.posapp.data.repository;

import com.tokopedia.posapp.domain.model.cart.ATCStatusDomain;
import com.tokopedia.posapp.domain.model.cart.CartDomain;

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
