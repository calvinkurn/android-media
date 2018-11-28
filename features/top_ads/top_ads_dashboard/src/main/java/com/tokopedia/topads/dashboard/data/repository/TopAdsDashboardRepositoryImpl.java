package com.tokopedia.topads.dashboard.data.repository;

import com.tokopedia.topads.dashboard.data.model.DataCredit;
import com.tokopedia.topads.dashboard.data.model.DataStatistic;
import com.tokopedia.topads.dashboard.data.model.TotalAd;
import com.tokopedia.topads.dashboard.data.source.TopAdsDashboardDataSource;
import com.tokopedia.topads.dashboard.domain.repository.TopAdsDashboardRepository;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by hadi.putra on 24/04/18.
 */

public class TopAdsDashboardRepositoryImpl implements TopAdsDashboardRepository {

    private final TopAdsDashboardDataSource topAdsDashboardDataSource;

    public TopAdsDashboardRepositoryImpl(TopAdsDashboardDataSource topAdsDashboardDataSource) {
        this.topAdsDashboardDataSource = topAdsDashboardDataSource;
    }

    @Override
    public Observable<TotalAd> populateTotalAds(RequestParams requestParams) {
        return topAdsDashboardDataSource.populateTotalAd(requestParams);
    }

    @Override
    public Observable<DataStatistic> getStatistics(RequestParams requestParams) {
        return topAdsDashboardDataSource.getStatistics(requestParams);
    }

    @Override
    public Observable<List<DataCredit>> getDashboardCredit(RequestParams requestParams){
        return topAdsDashboardDataSource.getDashboardCredit(requestParams);
    }
}
