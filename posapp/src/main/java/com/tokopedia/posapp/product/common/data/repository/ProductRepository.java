package com.tokopedia.posapp.product.common.data.repository;

import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 8/9/17.
 */

public interface ProductRepository {

    Observable<ProductDetailData> getProduct(RequestParams requestParams);

    Observable<ProductDomain> getProductDomain(RequestParams requestParams);

    Observable<List<ProductDomain>> getProductList(RequestParams requestParams);

    Observable<DataStatus> store(ProductListDomain productModel);
}
