package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.domain.model.DataStatus;
import com.tokopedia.posapp.domain.model.product.ProductListDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/9/17.
 */

public interface ProductRepository {
    Observable<ProductDetailData> getProduct(RequestParams requestParams);

    Observable<ProductListDomain> getProductList(RequestParams requestParams);

    Observable<DataStatus> storeProductToCache(ProductListDomain productModel);
}
