package com.tokopedia.topads.keyword.data.source;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.model.cloud.Datum;
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;
import com.tokopedia.topads.keyword.data.source.cloud.TopAdsKeywordDataSourceCloud;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by hadi.putra on 11/05/18.
 */

public class TopAdsKeywordDataSource {
    private final TopAdsKeywordDataSourceCloud dataSourceCloud;

    public TopAdsKeywordDataSource(TopAdsKeywordDataSourceCloud dataSourceCloud) {
        this.dataSourceCloud = dataSourceCloud;
    }

    public Observable<PageDataResponse<List<Datum>>> searchKeyword(RequestParams requestParams){
        return dataSourceCloud.searchKeyword(requestParams);
    }

    public Observable<PageDataResponse<DataBulkKeyword>> bulkActionKeyword(DataRequest<DataBulkKeyword> dataBulkKeyword){
        return dataSourceCloud.actionBulkKeyword(dataBulkKeyword);
    }

    public Observable<AddKeywordDomainModel> addKeywords(AddKeywordDomainModel addKeywordDomainModel) {
        return dataSourceCloud.addKeywords(addKeywordDomainModel);
    }
}
