package com.tokopedia.topads.dashboard.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.topads.dashboard.data.source.TopAdsCheckProductPromoDataSource;
import com.tokopedia.topads.dashboard.domain.TopAdsCheckProductPromoRepository;

import rx.Observable;

/**
 * Created by hadi-putra on 11/04/18.
 */

public class TopAdsCheckProductPromoRepositoryImpl implements TopAdsCheckProductPromoRepository {

    private final TopAdsCheckProductPromoDataSource dataSource;

    public TopAdsCheckProductPromoRepositoryImpl(TopAdsCheckProductPromoDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Observable<String> getProductPromoTopAds(RequestParams requestParams) {
        return dataSource.checkPromoAds(requestParams);
    }
}
