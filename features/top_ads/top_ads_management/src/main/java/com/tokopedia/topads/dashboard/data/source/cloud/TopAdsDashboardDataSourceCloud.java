package com.tokopedia.topads.dashboard.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsDashboardApi;
import com.tokopedia.usecase.RequestParams;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by hadi.putra on 24/04/18.
 */

public class TopAdsDashboardDataSourceCloud {

    private final TopAdsDashboardApi topAdsDashboardApi;

    public TopAdsDashboardDataSourceCloud(TopAdsDashboardApi topAdsDashboardApi) {
        this.topAdsDashboardApi = topAdsDashboardApi;
    }

    public Observable<TotalAd> populateTotalAds(RequestParams requestParams){
        return topAdsDashboardApi.populateTotalAd(requestParams.getParamsAllValueInString())
                .map(new Func1<Response<DataResponse<TotalAd>>, TotalAd>() {
                    @Override
                    public TotalAd call(Response<DataResponse<TotalAd>> dataResponse) {
                        return dataResponse.body().getData();
                    }
                });
    }
}
