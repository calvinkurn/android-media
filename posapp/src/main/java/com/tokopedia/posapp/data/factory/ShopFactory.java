package com.tokopedia.posapp.data.factory;

import com.tokopedia.posapp.data.mapper.GetShopEtalaseMapper;
import com.tokopedia.posapp.data.mapper.GetShopProductMapper;
import com.tokopedia.posapp.data.mapper.GetShopMapper;
import com.tokopedia.posapp.data.source.cloud.ShopCloudSource;
import com.tokopedia.posapp.data.source.cloud.api.AceApi;
import com.tokopedia.posapp.data.source.cloud.api.ShopApi;
import com.tokopedia.posapp.data.source.cloud.api.TomeApi;
import com.tokopedia.posapp.data.source.local.ShopLocalSource;

import javax.inject.Inject;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopFactory {
    private ShopApi shopApi;
    private AceApi aceApi;
    private TomeApi tomeApi;
    private GetShopMapper shopMapper;
    private GetShopProductMapper getShopProductMapper;
    private GetShopEtalaseMapper getShopEtalaseMapper;

    @Inject
    public ShopFactory(ShopApi shopApi,
                       AceApi aceApi,
                       TomeApi tomeApi,
                       GetShopMapper shopMapper,
                       GetShopProductMapper getShopProductMapper,
                       GetShopEtalaseMapper getShopEtalaseMapper) {
        this.shopApi = shopApi;
        this.aceApi = aceApi;
        this.tomeApi = tomeApi;
        this.shopMapper = shopMapper;
        this.getShopProductMapper = getShopProductMapper;
        this.getShopEtalaseMapper = getShopEtalaseMapper;
    }

    public ShopCloudSource cloud() {
        return new ShopCloudSource(shopApi, aceApi, tomeApi, shopMapper, getShopProductMapper, getShopEtalaseMapper);
    }

    public ShopLocalSource local() {
        return new ShopLocalSource();
    }
}
