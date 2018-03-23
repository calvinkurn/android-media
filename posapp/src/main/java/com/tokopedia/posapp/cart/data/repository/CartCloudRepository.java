package com.tokopedia.posapp.cart.data.repository;

import com.tokopedia.posapp.cart.data.source.CartLocalSource;
import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;
import com.tokopedia.posapp.cart.domain.model.CartDomain;
import com.tokopedia.posapp.product.common.data.source.cloud.ProductCloudSource;

import java.util.List;

import rx.Observable;

/**
 * @author okasurya on 3/21/18.
 */

public class CartCloudRepository implements CartRepository {
    private CartLocalSource cartLocalSource;
    private ProductCloudSource productCloudSource;

    private CartCloudRepository(CartLocalSource cartLocalSource,
                                ProductCloudSource productCloudSource) {
        this.cartLocalSource = cartLocalSource;
        this.productCloudSource = productCloudSource;
    }

    @Override
    public Observable<ATCStatusDomain> storeCartProduct(CartDomain cartDomain) {
        return null;
    }

    @Override
    public Observable<ATCStatusDomain> updateCartProduct(CartDomain cartDomain) {
        return null;
    }

    @Override
    public Observable<ATCStatusDomain> deleteCartProduct(CartDomain cartDomain) {
        return null;
    }

    @Override
    public Observable<ATCStatusDomain> deleteCart() {
        return null;
    }

    @Override
    public Observable<CartDomain> getCartProduct(int productId) {
        return null;
    }

    @Override
    public Observable<List<CartDomain>> getAllCartProducts() {
        return null;
    }
}
