package com.tokopedia.topads.dashboard.domain;

import com.tokopedia.core.base.domain.RequestParams;

import rx.Observable;

/**
 * Created by nakama on 17/04/18.
 */

public interface TopAdsCheckProductPromoRepository {
    Observable<String> getProductPromoTopAds(RequestParams requestParams);
}
