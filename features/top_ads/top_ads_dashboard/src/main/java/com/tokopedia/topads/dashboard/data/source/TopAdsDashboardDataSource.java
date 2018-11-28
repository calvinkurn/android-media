package com.tokopedia.topads.dashboard.data.source;

import com.tokopedia.topads.dashboard.data.model.DataCredit;
import com.tokopedia.topads.dashboard.data.model.DataStatistic;
import com.tokopedia.topads.dashboard.data.model.TotalAd;
import com.tokopedia.topads.dashboard.data.source.cloud.TopAdsDashboardDataSourceCloud;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by hadi.putra on 24/04/18.
 */

public class TopAdsDashboardDataSource {
    private final TopAdsDashboardDataSourceCloud topAdsDashboardDataSourceCloud;

    public TopAdsDashboardDataSource(TopAdsDashboardDataSourceCloud topAdsDashboardDataSourceCloud) {
        this.topAdsDashboardDataSourceCloud = topAdsDashboardDataSourceCloud;
    }

    public Observable<TotalAd> populateTotalAd(RequestParams requestParams){
        return topAdsDashboardDataSourceCloud.populateTotalAds(requestParams);
    }

    public Observable<DataStatistic> getStatistics(RequestParams requestParams) {
        return topAdsDashboardDataSourceCloud.getStatistics(requestParams);
    }

    public Observable<List<DataCredit>> getDashboardCredit(RequestParams requestParams) {
        return topAdsDashboardDataSourceCloud.getDashboardCredit(requestParams);
    }
}
