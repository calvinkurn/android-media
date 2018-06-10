package com.tokopedia.topads.keyword.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;
import com.tokopedia.topads.keyword.data.source.TopAdsKeywordActionBulkDataSource;
import com.tokopedia.topads.keyword.domain.TopAdsKeywordActionBulkRepository;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/29/17.
 */

public class TopAdsKeywordDeleteRepositoryImpl implements TopAdsKeywordActionBulkRepository {
    private final TopAdsKeywordActionBulkDataSource topAdsKeywordActionBulkDataSource;

    public TopAdsKeywordDeleteRepositoryImpl(TopAdsKeywordActionBulkDataSource topAdsKeywordActionBulkDataSource) {
        this.topAdsKeywordActionBulkDataSource = topAdsKeywordActionBulkDataSource;
    }

    @Override
    public Observable<PageDataResponse<DataBulkKeyword>> actionBulk(RequestParams requestParams) {
        return topAdsKeywordActionBulkDataSource.actionBulk(requestParams);
    }
}
