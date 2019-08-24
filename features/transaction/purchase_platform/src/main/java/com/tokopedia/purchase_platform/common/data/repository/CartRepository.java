package com.tokopedia.purchase_platform.common.data.repository;

import com.tokopedia.purchase_platform.common.data.apiservice.CartApi;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public class CartRepository implements ICartRepository {

    private CartApi cartApi;

//    @Inject
    public CartRepository(CartApi cartApi) {
        this.cartApi = cartApi;
    }

}
