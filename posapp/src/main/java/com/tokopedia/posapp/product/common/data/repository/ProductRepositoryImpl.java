package com.tokopedia.posapp.product.common.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.ProductFactory;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/9/17.
 */

public class ProductRepositoryImpl implements ProductRepository {
    ProductFactory productFactory;

    public ProductRepositoryImpl(ProductFactory productFactory) {
        this.productFactory = productFactory;
    }

    @Override
    public Observable<ProductDetailData> getProduct(RequestParams requestParams) {
        return productFactory.cloud().getProduct(requestParams);
    }

    @Override
    public Observable<ProductListDomain> getProductList(RequestParams requestParams) {
        return productFactory.cloudGateway().getProductList(requestParams);
    }

    @Override
    public Observable<DataStatus> storeProductsToCache(ProductListDomain productListDomain) {
        return productFactory.local().storeProduct(productListDomain);
    }
}
