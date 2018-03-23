package com.tokopedia.posapp.product.common.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/9/17.
 */

public interface ProductRepository {
    String OFFSET = "OFFSET";
    String LIMIT = "LIMIT";

    String KEYWORD = "KEYWORD";
    String ETALASE_ID = "ETALASE_ID";

    String PRODUCT_ID = "PRODUCT_ID";

    @Deprecated
    Observable<ProductDetailData> getProduct(RequestParams requestParams);

    Observable<ProductDomain> getProductDomain(RequestParams requestParams);

    Observable<ProductListDomain> getProductList(RequestParams requestParams);

    Observable<DataStatus> store(ProductListDomain productModel);
}
