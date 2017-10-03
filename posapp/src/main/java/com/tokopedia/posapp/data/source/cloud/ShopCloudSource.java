package com.tokopedia.posapp.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.data.mapper.GetShopMapper;
import com.tokopedia.posapp.data.source.cloud.api.AceApi;
import com.tokopedia.posapp.data.source.cloud.api.ShopApi;
import com.tokopedia.posapp.domain.model.shop.ShopDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopCloudSource {
    private ShopApi shopApi;
    private GetShopMapper getShopMapper;

    public ShopCloudSource(ShopApi shopApi,
                           GetShopMapper getShopMapper) {
        this.shopApi = shopApi;
        this.getShopMapper = getShopMapper;
    }

    public Observable<ShopDomain> getShop(RequestParams params) {
        return shopApi.getInfo(params.getParamsAllValueInString()).map(getShopMapper);
    }
}
