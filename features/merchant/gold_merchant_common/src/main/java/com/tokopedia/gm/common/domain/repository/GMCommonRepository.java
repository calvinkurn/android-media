package com.tokopedia.gm.common.domain.repository;

import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface GMCommonRepository {

    Observable<List<GMFeaturedProduct>> getFeaturedProductList(String shopId);

}