package com.tokopedia.shop.newproduct.view.datamodel;

import com.tokopedia.shop.newproduct.view.adapter.ShopProductAdapterTypeFactory;

public class HideViewModel implements BaseShopProductViewModel {

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
