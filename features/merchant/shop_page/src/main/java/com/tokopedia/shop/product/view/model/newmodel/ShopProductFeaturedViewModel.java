package com.tokopedia.shop.product.view.model.newmodel;

import com.tokopedia.shop.product.view.adapter.newadapter.ShopProductAdapterTypeFactory;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductFeaturedViewModel implements BaseShopProductViewModel{

    //TODO feature model

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
