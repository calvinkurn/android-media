package com.tokopedia.posapp.data.factory;

import com.tokopedia.posapp.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.data.mapper.GetShopMapper;
import com.tokopedia.posapp.data.source.cloud.ShopCloudSource;
import com.tokopedia.posapp.data.source.cloud.api.ShopApi;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopFactory {
    ShopApi shopApi;
    GetShopMapper shopMapper;
    GetProductListMapper getProductListMapper;

    public ShopFactory(ShopApi shopApi,
                       GetShopMapper shopMapper,
                       GetProductListMapper getProductListMapper) {
        this.shopApi = shopApi;
        this.shopMapper = shopMapper;
        this.getProductListMapper = getProductListMapper;
    }

    public ShopCloudSource getShopFromCloud() {
        return new ShopCloudSource(shopApi, shopMapper, getProductListMapper);
    }
}
