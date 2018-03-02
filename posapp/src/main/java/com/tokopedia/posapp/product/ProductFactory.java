package com.tokopedia.posapp.product;

import com.tokopedia.posapp.cache.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.product.productdetail.data.source.cloud.ProductCloudSource;
import com.tokopedia.posapp.product.productlist.data.source.cloud.ProductListApi;
import com.tokopedia.posapp.product.productlist.data.source.cloud.ProductListCloudSource;
import com.tokopedia.posapp.product.productlist.data.source.local.ProductLocalSource;
import com.tokopedia.posapp.product.productdetail.data.mapper.GetProductMapper;
import com.tokopedia.posapp.product.productdetail.data.source.cloud.api.ProductApi;

/**
 * Created by okasurya on 8/9/17.
 */
@Deprecated
public class ProductFactory {
    private ProductApi productApi;
    private ProductListApi productListApi;
    private GetProductMapper getProductMapper;
    private GetProductListMapper getProductListMapper;

    public ProductFactory(ProductListApi productListApi,
                          ProductApi productApi,
                          GetProductMapper getProductMapper,
                          GetProductListMapper getProductListMapper) {
        this.productListApi = productListApi;
        this.productApi = productApi;
        this.getProductMapper = getProductMapper;
        this.getProductListMapper = getProductListMapper;
    }

    public ProductCloudSource cloud() {
        return new ProductCloudSource(productApi, getProductMapper);
    }

    public ProductListCloudSource cloudGateway() {
        return new ProductListCloudSource(productListApi, getProductListMapper);
    }

    public ProductLocalSource local() {
        return new ProductLocalSource();
    }
}
