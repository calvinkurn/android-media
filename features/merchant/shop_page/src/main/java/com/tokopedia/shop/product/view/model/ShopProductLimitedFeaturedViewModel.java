package com.tokopedia.shop.product.view.model;

import com.tokopedia.shop.product.view.adapter.ShopProductLimitedAdapterTypeFactory;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductLimitedFeaturedViewModel extends ShopProductViewModel<ShopProductLimitedAdapterTypeFactory> implements ShopProductBaseViewModel{

    @Override
    public int type(ShopProductLimitedAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
