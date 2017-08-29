package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.product.model.productdetail.ProductCampaign;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;
import com.tokopedia.posapp.data.factory.ProductFactory;
import com.tokopedia.posapp.domain.model.product.ProductDomain;

import java.util.List;

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
        return productFactory.productFromCloud().getProduct(requestParams);
    }

    @Override
    public Observable<ProductCampaign> getProductCampaign(RequestParams requestParams) {
        return productFactory.productFromCloud().getProductCampaign(requestParams);
    }
}
