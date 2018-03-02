package com.tokopedia.posapp.product.productdetail.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.product.productdetail.data.mapper.GetProductMapper;
import com.tokopedia.posapp.product.productdetail.data.source.cloud.api.ProductApi;

import rx.Observable;

/**
 * Created by okasurya on 8/9/17.
 */

public class ProductCloudSource {
    public static final String PRODUCT_ID = "PRODUCT_ID";

    private ProductApi productApi;
    private GetProductMapper getProductMapper;

    public ProductCloudSource(ProductApi productApi,
                              GetProductMapper getProductMapper) {
        this.productApi = productApi;
        this.getProductMapper = getProductMapper;
    }

    public Observable<ProductDetailData> getProduct(RequestParams params) {
        return productApi.getProductDetail(params.getParamsAllValueInString()).map(getProductMapper);
    }
}
