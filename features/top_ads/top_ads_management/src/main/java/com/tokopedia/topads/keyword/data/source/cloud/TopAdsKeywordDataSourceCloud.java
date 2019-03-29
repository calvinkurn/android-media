package com.tokopedia.topads.keyword.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.mapper.KeywordAddDomainDataMapper;
import com.tokopedia.topads.keyword.data.model.cloud.Datum;
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;
import com.tokopedia.topads.keyword.data.model.cloud.request.keywordadd.AddKeywordRequest;
import com.tokopedia.topads.keyword.data.source.cloud.api.KeywordApi;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

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

    public Observable<PageDataResponse<DataBulkKeyword>> actionBulkKeyword(DataRequest<DataBulkKeyword> dataBulkKeywordDataRequest){
        return keywordApi.actionBulkKeyword(dataBulkKeywordDataRequest)
                .map(new Func1<Response<PageDataResponse<DataBulkKeyword>>, PageDataResponse<DataBulkKeyword>>() {
            @Override
            public PageDataResponse<DataBulkKeyword> call(Response<PageDataResponse<DataBulkKeyword>> pageDataResponse) {
                return pageDataResponse.body();
            }
        });
    }

    public Observable<AddKeywordDomainModel> addKeywords(AddKeywordDomainModel addKeywordDomainModel) {
        AddKeywordRequest addKeywordRequest = KeywordAddDomainDataMapper.convertDomainToRequestData(addKeywordDomainModel);
        return keywordApi.addKeyword(addKeywordRequest)
                .map(new KeywordAddDomainDataMapper());
    }
}
