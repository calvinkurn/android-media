package com.tokopedia.topads.keyword.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.model.cloud.Datum;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public interface TopAdsKeywordGetDetailRepository {
    Observable<PageDataResponse<List<Datum>>> getKeywordDetail(RequestParams requestParams);
}
