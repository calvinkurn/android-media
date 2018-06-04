package com.tokopedia.shop.product.view.model;

import com.tokopedia.shop.product.view.adapter.ShopProductLimitedAdapterTypeFactory;

/**
 * Created by zulfikarrahman on 6/4/18.
 */

public class ShopProductMoreViewModel implements ShopProductBaseViewModel {
    @Override
    public int type(ShopProductLimitedAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
