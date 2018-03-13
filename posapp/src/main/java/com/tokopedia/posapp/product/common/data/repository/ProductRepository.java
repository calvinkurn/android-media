package com.tokopedia.posapp.product.common.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/9/17.
 */

public interface ProductRepository {
    Observable<ProductDetailData> getProduct(RequestParams requestParams);

    Observable<ProductListDomain> getProductList(RequestParams requestParams);

    Observable<DataStatus> storeProductsToCache(ProductListDomain productModel);
}
