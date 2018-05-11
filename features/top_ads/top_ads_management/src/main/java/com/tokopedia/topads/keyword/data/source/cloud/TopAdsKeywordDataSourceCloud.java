package com.tokopedia.topads.keyword.data.source.cloud;

import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.model.cloud.Datum;
import com.tokopedia.topads.keyword.data.source.cloud.api.KeywordApi;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by hadi.putra on 11/05/18.
 */

public class TopAdsKeywordDataSourceCloud {

    private final KeywordApi keywordApi;

    public TopAdsKeywordDataSourceCloud(KeywordApi keywordApi) {
        this.keywordApi = keywordApi;
    }

    public Observable<PageDataResponse<List<Datum>>> searchKeyword(RequestParams requestParams){
        return keywordApi.getDashboardKeyword(requestParams.getParamsAllValueInString());
    }
}
