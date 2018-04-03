package com.tokopedia.gm.featured.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.common.featuredproduct.GMFeaturedProductDomainModel;
import com.tokopedia.gm.featured.domain.model.GMFeaturedProductSubmitDomainModel;

import rx.Observable;

/**
 * Created by normansyahputa on 9/6/17.
 */

public interface GMFeaturedProductRepository {
    Observable<GMFeaturedProductSubmitDomainModel> postFeatureProductData(RequestParams requestParams);

    Observable<GMFeaturedProductDomainModel> getFeatureProductData(RequestParams requestParams);
}
