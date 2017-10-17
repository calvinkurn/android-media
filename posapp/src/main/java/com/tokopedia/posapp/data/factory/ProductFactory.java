package com.tokopedia.posapp.data.factory;

import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.posapp.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.data.mapper.GetProductMapper;
import com.tokopedia.posapp.data.source.cloud.ProductCloudSource;
import com.tokopedia.posapp.data.source.cloud.api.AceApi;
import com.tokopedia.posapp.data.source.cloud.api.ProductApi;
import com.tokopedia.posapp.data.source.cloud.api.pos.PosProductApi;
import com.tokopedia.posapp.data.source.cloud.pos.PosProductCloudSource;
import com.tokopedia.posapp.data.source.local.ProductLocalSource;

/**
 * Created by okasurya on 8/9/17.
 */

public class ProductFactory {
    private PosProductApi posProductApi;
    private ProductApi productApi;
    private MojitoApi mojitoApi;
    private AceApi aceApi;
    private GetProductMapper getProductMapper;
    private GetProductListMapper getProductListMapper;

    public ProductFactory(PosProductApi posProductApi,
                          ProductApi productApi,
                          MojitoApi mojitoApi,
                          AceApi aceApi,
                          GetProductMapper getProductMapper,
                          GetProductListMapper getProductListMapper) {
        this.posProductApi = posProductApi;
        this.productApi = productApi;
        this.mojitoApi = mojitoApi;
        this.aceApi = aceApi;
        this.getProductMapper = getProductMapper;
        this.getProductListMapper = getProductListMapper;
    }

    public ProductCloudSource cloud() {
        return new ProductCloudSource(productApi, mojitoApi, aceApi, getProductMapper, getProductListMapper);
    }

    public PosProductCloudSource cloudGateway() {
        return new PosProductCloudSource(posProductApi, getProductListMapper);
    }

    public ProductLocalSource local() {
        return new ProductLocalSource();
    }
}
