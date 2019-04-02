package com.tokopedia.topads.common.domain.repository;

import com.tokopedia.topads.common.data.model.DataCheckPromo;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by hadi.putra on 17/04/18.
 */

public interface TopAdsCheckProductPromoRepository {
    Observable<DataCheckPromo> getProductPromoTopAds(RequestParams requestParams);
}
