package com.tokopedia.topads.keyword.domain;

import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/29/17.
 */

public interface TopAdsKeywordActionBulkRepository {
    Observable<PageDataResponse<DataBulkKeyword>> actionBulk(RequestParams requestParams);
}
