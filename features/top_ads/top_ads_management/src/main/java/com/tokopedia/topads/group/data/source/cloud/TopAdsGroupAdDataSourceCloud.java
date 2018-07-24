package com.tokopedia.topads.group.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdBulkAction;
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

    public Observable<GroupAdBulkAction> bulkAction(DataRequest<GroupAdBulkAction> dataRequest) {
        return topAdsGroupAdApi.bulkActionGroupAd(dataRequest).map(new Func1<Response<DataResponse<GroupAdBulkAction>>, GroupAdBulkAction>() {
            @Override
            public GroupAdBulkAction call(Response<DataResponse<GroupAdBulkAction>> dataResponse) {
                return dataResponse.body().getData();
            }
        });
    }

    public Observable<List<GroupAd>> searchGroupAd(RequestParams requestParams){
        return topAdsGroupAdApi.searchGroupAd(requestParams.getParamsAllValueInString())
                .map(new Func1<Response<DataResponse<List<GroupAd>>>, List<GroupAd>>() {
                    @Override
                    public List<GroupAd> call(Response<DataResponse<List<GroupAd>>> dataResponse) {
                        return dataResponse.body().getData();
                    }
                });
    }
}
