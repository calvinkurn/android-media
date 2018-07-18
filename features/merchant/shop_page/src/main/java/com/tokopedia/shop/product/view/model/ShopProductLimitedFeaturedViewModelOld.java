package com.tokopedia.shop.product.view.model;

import com.tokopedia.shop.product.view.adapter.ShopProductLimitedAdapterTypeFactory;

/**
 * Created by zulfikarrahman on 1/16/18.
 */
@Deprecated
public class ShopProductLimitedFeaturedViewModelOld extends ShopProductViewModelOld<ShopProductLimitedAdapterTypeFactory> implements ShopProductBaseViewModel{

    @Override
    public int type(ShopProductLimitedAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
