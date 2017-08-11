package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.product.model.productdetail.ProductCampaign;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.domain.model.product.ProductDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/9/17.
 */

public interface ProductRepository {
    Observable<ProductDetailData> getProduct(RequestParams requestParams);

    Observable<ProductCampaign> getProductCampaign(RequestParams requestParams);
}
