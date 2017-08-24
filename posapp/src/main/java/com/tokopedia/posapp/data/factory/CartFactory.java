package com.tokopedia.posapp.data.factory;

import com.tokopedia.posapp.data.mapper.AddToCartMapper;
import com.tokopedia.posapp.data.source.local.CartLocalSource;

/**
 * Created by okasurya on 8/22/17.
 */

public class CartFactory {
    AddToCartMapper addToCartMapper;

    public CartFactory(AddToCartMapper addToCartMapper) {
        this.addToCartMapper = addToCartMapper;
    }

    public CartLocalSource getCartFromLocal() {
        return new CartLocalSource(addToCartMapper);
    }
}
