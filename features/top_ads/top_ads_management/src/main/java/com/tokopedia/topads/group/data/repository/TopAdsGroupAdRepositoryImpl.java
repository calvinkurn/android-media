package com.tokopedia.topads.group.data.repository;

import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.group.data.source.TopAdsGroupAdDataSource;
import com.tokopedia.topads.group.domain.repository.TopAdsGroupAdRepository;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by hadi.putra on 09/05/18.
 */

public class TopAdsGroupAdRepositoryImpl implements TopAdsGroupAdRepository {
    private final TopAdsGroupAdDataSource dataSource;

    public TopAdsGroupAdRepositoryImpl(TopAdsGroupAdDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Observable<PageDataResponse<List<GroupAd>>> getGroupAd(RequestParams requestParams) {
        return dataSource.getGroupAd(requestParams);
    }
}
