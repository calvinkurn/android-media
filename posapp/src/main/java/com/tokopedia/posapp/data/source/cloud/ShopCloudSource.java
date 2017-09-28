package com.tokopedia.posapp.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.mapper.GetShopProductMapper;
import com.tokopedia.posapp.data.mapper.GetShopMapper;
import com.tokopedia.posapp.data.source.cloud.api.AceApi;
import com.tokopedia.posapp.data.source.cloud.api.ShopApi;
import com.tokopedia.posapp.domain.model.shop.ShopDomain;
import com.tokopedia.posapp.domain.model.shop.ProductListDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopCloudSource {
    private ShopApi shopApi;
    private AceApi aceApi;
    private GetShopMapper getShopMapper;
    private GetShopProductMapper getShopProductMapper;

    public ShopCloudSource(ShopApi shopApi,
                           AceApi aceApi,
                           GetShopMapper getShopMapper,
                           GetShopProductMapper getShopProductMapper) {
        this.shopApi = shopApi;
        this.aceApi = aceApi;
        this.getShopMapper = getShopMapper;
        this.getShopProductMapper = getShopProductMapper;
    }

    public Observable<ShopDomain> getShop(RequestParams params) {
        return shopApi.getInfo(params.getParamsAllValueInString()).map(getShopMapper);
    }
}
