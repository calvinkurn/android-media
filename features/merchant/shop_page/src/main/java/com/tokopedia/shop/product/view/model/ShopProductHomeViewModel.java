package com.tokopedia.shop.product.view.model;

import com.tokopedia.shop.product.view.adapter.ShopProductLimitedAdapterTypeFactory;

/**
 * Created by zulfikarrahman on 3/30/18.
 */

public class ShopProductHomeViewModel extends ShopProductViewModel<ShopProductLimitedAdapterTypeFactory> implements ShopProductBaseViewModel {
    @Override
    public int type(ShopProductLimitedAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
