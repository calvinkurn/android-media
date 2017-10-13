package com.tokopedia.posapp.data.source.cloud.pos;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.data.source.cloud.api.pos.PosProductApi;
import com.tokopedia.posapp.domain.model.shop.ProductListDomain;

import rx.Observable;

/**
 * Created by okasurya on 10/13/17.
 */

public class PosProductCloudSource {
    PosProductApi posProductApi;
    GetProductListMapper getProductListMapper;

    PosProductCloudSource(PosProductApi posProductApi,
                          GetProductListMapper getProductListMapper) {
        this.posProductApi = posProductApi;
        this.getProductListMapper = getProductListMapper;
    }

    Observable<ProductListDomain> getProductList(RequestParams params) {
        return posProductApi.getProductList(
            params.getString("shop_id", ""),
            params.getInt("start", 0),
            params.getInt("row", 10)
        ).map(getProductListMapper);
    }
}
