package com.tokopedia.topads.group.data.source.cloud;

import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.group.data.apiservice.TopAdsGroupAdApi;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by hadi.putra on 09/05/18.
 */

public class TopAdsGroupAdDataSourceCloud {
    private final TopAdsGroupAdApi topAdsGroupAdApi;

    public TopAdsGroupAdDataSourceCloud(TopAdsGroupAdApi topAdsGroupAdApi) {
        this.topAdsGroupAdApi = topAdsGroupAdApi;
    }

    public Observable<PageDataResponse<List<GroupAd>>> getGroupAd(RequestParams requestParams){
        return topAdsGroupAdApi.getGroupAd(requestParams.getParamsAllValueInString())
                .map(new Func1<Response<PageDataResponse<List<GroupAd>>>, PageDataResponse<List<GroupAd>>>() {
                    @Override
                    public PageDataResponse<List<GroupAd>> call(Response<PageDataResponse<List<GroupAd>>> pageDataResponse) {
                        return pageDataResponse.body();
                    }
                });
    }
}
