package com.tokopedia.posapp.data.source.cloud.pos;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.mapper.GetGatewayProductListMapper;
import com.tokopedia.posapp.data.source.cloud.api.GatewayProductApi;
import com.tokopedia.posapp.domain.model.product.ProductListDomain;

import rx.Observable;

/**
 * Created by okasurya on 10/13/17.
 */

public class GatewayProductCloudSource {
    private static final String SHOP_ID = "shop_id";

    private GatewayProductApi gatewayProductApi;
    private GetGatewayProductListMapper getProductListMapper;

    public GatewayProductCloudSource(GatewayProductApi gatewayProductApi,
                                     GetGatewayProductListMapper getProductListMapper) {
        this.gatewayProductApi = gatewayProductApi;
        this.getProductListMapper = getProductListMapper;
    }

    public Observable<ProductListDomain> getProductList(RequestParams params) {
        return gatewayProductApi
                .getProductList(params.getParamsAllValueInString())
                .map(getProductListMapper);
    }
}
