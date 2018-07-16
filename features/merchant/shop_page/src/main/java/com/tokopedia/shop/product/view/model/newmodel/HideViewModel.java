package com.tokopedia.shop.product.view.model.newmodel;

import com.tokopedia.shop.product.view.adapter.newadapter.ShopProductAdapterTypeFactory;

public class HideViewModel implements BaseShopProductViewModel {

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
