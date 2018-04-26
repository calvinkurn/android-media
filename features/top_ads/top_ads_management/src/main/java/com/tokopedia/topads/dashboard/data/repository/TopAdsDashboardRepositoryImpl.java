package com.tokopedia.topads.dashboard.data.repository;

import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.data.model.data.DataStatistic;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;
import com.tokopedia.topads.dashboard.data.source.TopAdsDashboardDataSource;
import com.tokopedia.topads.dashboard.domain.repository.TopAdsDashboardRepository;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

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
    public Observable<List<Cell>> getStatistics(RequestParams requestParams) {
        return topAdsDashboardDataSource.getStatistics(requestParams)
                .map(new Func1<DataStatistic, List<Cell>>() {
                    @Override
                    public List<Cell> call(DataStatistic dataStatistic) {
                        return dataStatistic.getCells();
                    }
                });
    }
}
