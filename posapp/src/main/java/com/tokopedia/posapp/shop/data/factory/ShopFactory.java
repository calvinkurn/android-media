package com.tokopedia.posapp.shop.data.factory;

import com.tokopedia.posapp.shop.data.GetShopMapper;
import com.tokopedia.posapp.shop.data.source.cloud.ShopCloudSource;
import com.tokopedia.posapp.shop.data.source.cloud.ShopApi;

import javax.inject.Inject;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopFactory {
    private ShopApi shopApi;
    private GetShopMapper shopMapper;

    @Inject
    public ShopFactory(ShopApi shopApi,
                       GetShopMapper shopMapper) {
        this.shopApi = shopApi;
        this.shopMapper = shopMapper;
    }

    public ShopCloudSource cloud() {
        return new ShopCloudSource(shopApi, shopMapper);
    }
}
