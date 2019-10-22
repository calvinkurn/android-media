package com.tokopedia.tkpd.home.wishlist.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.tkpd.home.wishlist.adapter.factory.WishlistTypeFactory;

/**
 * Author errysuprayogi on 25,July,2019
 */
public class WishlistProductViewModel implements Visitable<WishlistTypeFactory> {

    private ProductItem productItem;

    public WishlistProductViewModel(ProductItem productItem) {
        this.productItem = productItem;
    }

    public ProductItem getProductItem() {
        return productItem;
    }

    @Override
    public int type(WishlistTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
