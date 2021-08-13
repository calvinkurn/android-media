package com.tokopedia.home.explore.view.adapter.datamodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.explore.domain.model.ShopData;
import com.tokopedia.home.explore.view.adapter.TypeFactory;

/**
 * Created by errysuprayogi on 2/8/18.
 */

public class MyShopDataModel implements Visitable<TypeFactory> {

    private ShopData shopData;

    public MyShopDataModel(ShopData shopData) {
        this.shopData = shopData;
    }

    public ShopData getShopData() {
        return shopData;
    }

    public void setShopData(ShopData shopData) {
        this.shopData = shopData;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
