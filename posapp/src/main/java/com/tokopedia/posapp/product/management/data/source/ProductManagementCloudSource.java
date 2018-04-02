package com.tokopedia.posapp.product.management.data.source;

import com.tokopedia.posapp.product.productlist.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 3/20/18.
 */

public class ProductManagementCloudSource {
    private ProductManagementApi productManagementApi;
    private GetProductListMapper getProductListManagementMapper;

    @Inject
    public ProductManagementCloudSource(ProductManagementApi productManagementApi,
                                        GetProductListMapper getProductListManagementMapper) {
        this.productManagementApi = productManagementApi;
        this.getProductListManagementMapper = getProductListManagementMapper;
    }

    public Observable<ProductListDomain> getProductList(RequestParams requestParams) {
        return productManagementApi
                .getProducts(requestParams.getParamsAllValueInString())
                .map(getProductListManagementMapper);
    }
}
