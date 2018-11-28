package com.tokopedia.topads.dashboard.domain.repository;

import com.tokopedia.topads.dashboard.data.model.DataCredit;
import com.tokopedia.topads.dashboard.data.model.DataStatistic;
import com.tokopedia.topads.dashboard.data.model.TotalAd;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by nakama on 24/04/18.
 */

public interface TopAdsDashboardRepository {

    Observable<TotalAd> populateTotalAds(RequestParams requestParams);

    Observable<DataStatistic> getStatistics(RequestParams requestParams);

    Observable<List<DataCredit>> getDashboardCredit(RequestParams requestParams);
}
