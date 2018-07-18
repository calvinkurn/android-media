package com.tokopedia.shop.product.view.model.newmodel;

import com.tokopedia.shop.product.view.adapter.newadapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.listener.ShopProductUserVisibleHintListener;

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
