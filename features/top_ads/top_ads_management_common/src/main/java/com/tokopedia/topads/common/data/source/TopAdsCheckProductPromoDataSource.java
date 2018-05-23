package com.tokopedia.topads.common.data.source;
;
import com.tokopedia.topads.common.data.model.DataCheckPromo;
import com.tokopedia.topads.common.data.source.cloud.TopAdsCheckProductPromoDataSourceCloud;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by hadi-puta on 11/04/18.
 */

public class TopAdsCheckProductPromoDataSource {
    private final TopAdsCheckProductPromoDataSourceCloud dataSourceCloud;

    public TopAdsCheckProductPromoDataSource(TopAdsCheckProductPromoDataSourceCloud dataSourceCloud) {
        this.dataSourceCloud = dataSourceCloud;
    }

    public Observable<DataCheckPromo> checkPromoAds(RequestParams requestParams) {
        return dataSourceCloud.checkPromoAds(requestParams);
    }
}
