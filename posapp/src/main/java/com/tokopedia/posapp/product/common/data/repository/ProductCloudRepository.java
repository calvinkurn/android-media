package com.tokopedia.posapp.product.common.data.repository;

import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.posapp.product.common.data.source.cloud.ProductCloudSource;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

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
    public Observable<List<ProductDomain>> getProductList(RequestParams requestParams) {
        return productCloudSource.getProductList(requestParams.getString(ProductConstant.Key.OUTLET_ID, ""), requestParams);
    }

    @Override
    public Observable<DataStatus> store(ProductListDomain productListDomain) {
        return Observable.error(new RuntimeException("Not Implemented"));
    }
}
