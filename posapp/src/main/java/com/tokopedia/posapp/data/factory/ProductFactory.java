package com.tokopedia.posapp.data.factory;

import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.posapp.data.mapper.GetProductMapper;
import com.tokopedia.posapp.data.mapper.GetShopProductMapper;
import com.tokopedia.posapp.data.source.cloud.ProductCloudSource;
import com.tokopedia.posapp.data.source.cloud.api.AceApi;
import com.tokopedia.posapp.data.source.cloud.api.ProductApi;
import com.tokopedia.posapp.data.source.local.ProductLocalSource;

/**
 * Created by okasurya on 8/9/17.
 */

public class ProductFactory {
    private ProductApi productApi;
    private MojitoApi mojitoApi;
    private AceApi aceApi;
    private GetProductMapper getProductMapper;
    private GetShopProductMapper getProductListMapper;

    public ProductFactory(ProductApi productApi,
                          MojitoApi mojitoApi,
                          AceApi aceApi,
                          GetProductMapper getProductMapper,
                          GetShopProductMapper getProductListMapper) {
        this.productApi = productApi;
        this.mojitoApi = mojitoApi;
        this.aceApi = aceApi;
        this.getProductMapper = getProductMapper;
        this.getProductListMapper = getProductListMapper;
    }

    public ProductCloudSource cloud() {
        return new ProductCloudSource(productApi, mojitoApi, aceApi, getProductMapper, getProductListMapper);
    }

    public ProductLocalSource local() {
        return new ProductLocalSource();
    }
}
