package com.tokopedia.posapp.product.common.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.di.qualifier.CloudSource;
import com.tokopedia.posapp.product.common.data.source.cloud.ProductCloudSource;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productdetail.view.Product;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;

/**
 * Created by okasurya on 8/9/17.
 */

public class ProductCloudRepository implements ProductRepository {
    private ProductCloudSource productCloudSource;

    @Inject
    public ProductCloudRepository(ProductCloudSource productCloudSource) {
        this.productCloudSource = productCloudSource;
    }

    @Override
    public Observable<ProductDetailData> getProduct(RequestParams requestParams) {
        return productCloudSource.getProduct(requestParams);
    }

    @Override
    public Observable<ProductDomain> getProductDomain(RequestParams requestParams) {
        return null;
    }

    @Override
    public Observable<ProductListDomain> getProductList(RequestParams requestParams) {
        return productCloudSource.getProductList(requestParams);
    }

    @Override
    public Observable<DataStatus> store(ProductListDomain productListDomain) {
        return Observable.error(new RuntimeException("Not Implemented"));
    }
}
