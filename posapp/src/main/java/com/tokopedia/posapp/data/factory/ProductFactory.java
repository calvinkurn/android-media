package com.tokopedia.posapp.data.factory;

import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.posapp.data.mapper.GetGatewayProductListMapper;
import com.tokopedia.posapp.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.data.mapper.GetProductMapper;
import com.tokopedia.posapp.data.source.cloud.ProductCloudSource;
import com.tokopedia.posapp.data.source.cloud.api.AceApi;
import com.tokopedia.posapp.data.source.cloud.api.ProductApi;
import com.tokopedia.posapp.data.source.cloud.api.GatewayProductApi;
import com.tokopedia.posapp.data.source.cloud.pos.GatewayProductCloudSource;
import com.tokopedia.posapp.data.source.local.ProductLocalSource;

/**
 * Created by okasurya on 8/9/17.
 */

public class ProductFactory {
    private GatewayProductApi gatewayProductApi;
    private ProductApi productApi;
    private MojitoApi mojitoApi;
    private AceApi aceApi;
    private GetProductMapper getProductMapper;
    private GetProductListMapper getProductListMapper;
    private GetGatewayProductListMapper getGatewayProductListMapper;

    public ProductFactory(GatewayProductApi gatewayProductApi,
                          ProductApi productApi,
                          MojitoApi mojitoApi,
                          AceApi aceApi,
                          GetProductMapper getProductMapper,
                          GetProductListMapper getProductListMapper,
                          GetGatewayProductListMapper getGatewayProductListMapper) {
        this.gatewayProductApi = gatewayProductApi;
        this.productApi = productApi;
        this.mojitoApi = mojitoApi;
        this.aceApi = aceApi;
        this.getProductMapper = getProductMapper;
        this.getProductListMapper = getProductListMapper;
        this.getGatewayProductListMapper = getGatewayProductListMapper;
    }

    public ProductCloudSource cloud() {
        return new ProductCloudSource(productApi, mojitoApi, aceApi, getProductMapper, getProductListMapper);
    }

    public GatewayProductCloudSource cloudGateway() {
        return new GatewayProductCloudSource(gatewayProductApi, getGatewayProductListMapper);
    }

    public ProductLocalSource local() {
        return new ProductLocalSource();
    }
}
