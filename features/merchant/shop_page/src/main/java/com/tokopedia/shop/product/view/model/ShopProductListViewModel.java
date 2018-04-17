package com.tokopedia.shop.product.view.model;

import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;

/**
 * Created by zulfikarrahman on 3/30/18.
 */

public class ShopProductListViewModel extends ShopProductViewModel<ShopProductAdapterTypeFactory> {
    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
