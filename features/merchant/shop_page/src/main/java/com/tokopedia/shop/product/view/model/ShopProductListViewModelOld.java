package com.tokopedia.shop.product.view.model;

import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactoryOld;

/**
 * Created by zulfikarrahman on 3/30/18.
 */
@Deprecated
public class ShopProductListViewModelOld extends ShopProductViewModelOld<ShopProductAdapterTypeFactoryOld> {
    @Override
    public int type(ShopProductAdapterTypeFactoryOld typeFactory) {
        return typeFactory.type(this);
    }
}
