package com.tokopedia.opportunity.domain.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.opportunity.data.source.CloudProductDataSource;

import rx.Observable;

/**
 * Created by nakama on 31/03/18.
 */

public class ProductRepositoryImpl implements ProductRepository {
    private CloudProductDataSource cloudProductDataSource;

    public ProductRepositoryImpl(CloudProductDataSource cloudProductDataSource) {
        this.cloudProductDataSource = cloudProductDataSource;
    }

    @Override
    public Observable<ProductDetailData> getProduct(RequestParams params) {
        return cloudProductDataSource.getProductDetail(params);
    }
}
