package com.tokopedia.shop.product.view.model;

import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductEtalaseTitleViewModel implements BaseShopProductViewModel {

    private String etalaseName;

    public ShopProductEtalaseTitleViewModel(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
