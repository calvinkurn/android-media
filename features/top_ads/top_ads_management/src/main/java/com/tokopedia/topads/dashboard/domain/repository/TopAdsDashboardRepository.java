package com.tokopedia.topads.dashboard.domain.repository;

import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by nakama on 24/04/18.
 */

public interface TopAdsDashboardRepository {

    Observable<TotalAd> populateTotalAds(RequestParams requestParams);

    Observable<List<Cell>> getStatistics(RequestParams requestParams);
}
