package com.tokopedia.posapp.product.productlist.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.cache.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.product.productlist.data.source.cloud.ProductListApi;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;

import rx.Observable;

/**
 * Created by okasurya on 10/13/17.
 */

public class ProductListCloudSource {
    private static final String SHOP_ID = "shop_id";

    private ProductListApi productListApi;
    private GetProductListMapper getProductListMapper;

    public ProductListCloudSource(ProductListApi productListApi,
                                  GetProductListMapper getProductListMapper) {
        this.productListApi = productListApi;
        this.getProductListMapper = getProductListMapper;
    }

    public Observable<ProductListDomain> getProductList(RequestParams params) {
        return productListApi
                .getProductList(params.getParamsAllValueInString())
                .map(getProductListMapper);
    }
}
