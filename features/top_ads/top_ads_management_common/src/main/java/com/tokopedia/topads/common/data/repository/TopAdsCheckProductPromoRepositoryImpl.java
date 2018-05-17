package com.tokopedia.topads.common.data.repository;

import com.tokopedia.topads.common.data.model.DataCheckPromo;
import com.tokopedia.topads.common.data.source.TopAdsCheckProductPromoDataSource;
import com.tokopedia.topads.common.domain.repository.TopAdsCheckProductPromoRepository;
import com.tokopedia.usecase.RequestParams;

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
    public Observable<DataCheckPromo> getProductPromoTopAds(RequestParams requestParams) {
        return dataSource.checkPromoAds(requestParams);
    }
}
