package com.tokopedia.posapp.product.management.data.repository;

import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.management.data.source.ProductManagementCloudSource;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 3/20/18.
 */

public class ProductManagementCloudRepository implements ProductManagementRepository {
    private ProductManagementCloudSource productManagementCloudSource;

    @Inject
    public ProductManagementCloudRepository(ProductManagementCloudSource productManagementCloudSource) {
        this.productManagementCloudSource = productManagementCloudSource;
    }

    @Override
    public Observable<ProductListDomain> getList(RequestParams requestParams) {
        return productManagementCloudSource.getProductList(requestParams);
    }

    @Override
    public Observable<ProductDomain> get(RequestParams requestParams) {
        return null;
    }

    @Override
    public Observable<DataStatus> edit(RequestParams requestParams) {
        return null;
    }
}
