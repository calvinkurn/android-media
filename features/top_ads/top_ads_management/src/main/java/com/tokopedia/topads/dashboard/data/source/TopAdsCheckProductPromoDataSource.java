package com.tokopedia.topads.dashboard.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.topads.dashboard.data.source.cloud.TopAdsCheckProductPromoDataSourceCloud;

import rx.Observable;

/**
 * Created by hadi-puta on 11/04/18.
 */

public class TopAdsCheckProductPromoDataSource {
    private final TopAdsCheckProductPromoDataSourceCloud dataSourceCloud;

    public TopAdsCheckProductPromoDataSource(TopAdsCheckProductPromoDataSourceCloud dataSourceCloud) {
        this.dataSourceCloud = dataSourceCloud;
    }

    public Observable<String> checkPromoAds(RequestParams requestParams) {
        return dataSourceCloud.checkPromoAds(requestParams);
    }
}
