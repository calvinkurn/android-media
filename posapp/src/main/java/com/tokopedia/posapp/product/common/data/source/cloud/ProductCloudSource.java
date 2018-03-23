package com.tokopedia.posapp.product.common.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.cache.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.product.productdetail.data.mapper.GetProductMapper;
import com.tokopedia.posapp.product.productdetail.data.source.cloud.api.ProductApi;
import com.tokopedia.posapp.product.productlist.data.source.cloud.ProductListApi;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 8/9/17.
 */

public class ProductCloudSource {
    public static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String SHOP_ID = "shop_id";

    private ProductApi productApi;
    private ProductListApi productListApi;
    private GetProductMapper getProductMapper;
    private GetProductListMapper getProductListMapper;

    @Inject
    public ProductCloudSource(ProductApi productApi,
                              ProductListApi productListApi,
                              GetProductMapper getProductMapper,
                              GetProductListMapper getProductListMapper) {
        this.productApi = productApi;
        this.productListApi = productListApi;
        this.getProductMapper = getProductMapper;
        this.getProductListMapper = getProductListMapper;
    }

    public Observable<ProductDetailData> getProduct(RequestParams params) {
        return productApi.getProductDetail(params.getParamsAllValueInString()).map(getProductMapper);
    }

    public Observable<ProductListDomain> getProductList(RequestParams params) {
        return productListApi
                .getProductList(params.getParamsAllValueInString())
                .map(getProductListMapper);
    }
}
