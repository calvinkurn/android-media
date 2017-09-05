package com.tokopedia.posapp.data.factory;

import com.tokopedia.posapp.data.mapper.GetShopProductMapper;
import com.tokopedia.posapp.data.mapper.GetShopMapper;
import com.tokopedia.posapp.data.source.cloud.ShopCloudSource;
import com.tokopedia.posapp.data.source.cloud.api.AceApi;
import com.tokopedia.posapp.data.source.cloud.api.ShopApi;
import com.tokopedia.posapp.data.source.local.ShopLocalSource;

import javax.inject.Inject;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopFactory {
    private ShopApi shopApi;
    private AceApi aceApi;
    private GetShopMapper shopMapper;
    private GetShopProductMapper getShopProductMapper;

    @Inject
    public ShopFactory(ShopApi shopApi,
                       AceApi aceApi,
                       GetShopMapper shopMapper,
                       GetShopProductMapper getShopProductMapper) {
        this.shopApi = shopApi;
        this.aceApi = aceApi;
        this.shopMapper = shopMapper;
        this.getShopProductMapper = getShopProductMapper;
    }

    public ShopCloudSource getShopFromCloud() {
        return new ShopCloudSource(shopApi, aceApi, shopMapper, getShopProductMapper);
    }

    public ShopLocalSource getShopFromLocal() {
        return new ShopLocalSource();
    }
}
