package com.tokopedia.opportunity.domain.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;

import rx.Observable;

/**
 * Created by nakama on 31/03/18.
 */

public interface ProductRepository {
    public Observable<ProductDetailData> getProduct(RequestParams params);
}
