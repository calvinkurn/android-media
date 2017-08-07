package com.tokopedia.posapp.data.factory;

import com.tokopedia.posapp.data.mapper.GetShopMapper;
import com.tokopedia.posapp.data.source.cloud.ShopCloudSource;
import com.tokopedia.posapp.data.source.cloud.api.ShopApi;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopFactory {
    ShopApi shopApi;
    GetShopMapper shopMapper;

    public ShopFactory(ShopApi shopApi, GetShopMapper shopMapper) {
        this.shopApi = shopApi;
        this.shopMapper = shopMapper;
    }

    public ShopCloudSource getShopFromCloud() {
        return new ShopCloudSource(shopApi, shopMapper);
    }
}
