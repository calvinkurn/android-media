package com.tokopedia.topads.dashboard.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.data.model.data.DataStatistic;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsDashboardApi;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

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
                .map(new TopAdsResponseMapper<TotalAd>());
    }

    public Observable<DataStatistic> getStatistics(RequestParams requestParams) {
        return topAdsDashboardApi.getStatistics(requestParams.getParamsAllValueInString())
                .map(new TopAdsResponseMapper<DataStatistic>());
    }


    class TopAdsResponseMapper<E> implements Func1<Response<DataResponse<E>>, E> {

        @Override
        public E call(Response<DataResponse<E>> dataResponseResponse) {
            return dataResponseResponse.body().getData();
        }
    }
}
