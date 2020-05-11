package com.tokopedia.shop.product.view.datamodel;

import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;

public class HideViewModel implements BaseShopProductViewModel {

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
