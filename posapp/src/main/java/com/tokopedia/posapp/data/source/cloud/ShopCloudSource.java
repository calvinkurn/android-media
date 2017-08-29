package com.tokopedia.posapp.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;
import com.tokopedia.posapp.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.data.mapper.GetShopMapper;
import com.tokopedia.posapp.data.source.cloud.api.ShopApi;
import com.tokopedia.posapp.domain.model.shop.ShopDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopCloudSource {
    ShopApi shopApi;
    GetShopMapper getShopMapper;
    GetProductListMapper getProductListMapper;

    public ShopCloudSource(ShopApi shopApi,
                           GetShopMapper getShopMapper,
                           GetProductListMapper getProductListMapper) {
        this.shopApi = shopApi;
        this.getShopMapper = getShopMapper;
        this.getProductListMapper = getProductListMapper;
    }

    public Observable<ShopDomain> getShop(RequestParams params) {
        return shopApi.getInfo(params.getParamsAllValueInString()).map(getShopMapper);
    }

    public Observable<ProductModel> getProductList(RequestParams params) {
        return shopApi.getProductList(params.getParamsAllValueInString()).map(getProductListMapper);
    }
}
