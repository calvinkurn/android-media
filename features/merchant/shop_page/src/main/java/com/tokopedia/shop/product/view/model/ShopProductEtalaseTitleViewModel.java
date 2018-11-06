package com.tokopedia.shop.product.view.model;

import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductEtalaseTitleViewModel implements BaseShopProductViewModel {

    private String etalaseName;
    private String etalaseBadge;

    public ShopProductEtalaseTitleViewModel(String etalaseName, String etalaseBadge) {
        this.etalaseName = etalaseName;
        this.etalaseBadge = etalaseBadge;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public String getEtalaseBadge() {
        return etalaseBadge;
    }

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
