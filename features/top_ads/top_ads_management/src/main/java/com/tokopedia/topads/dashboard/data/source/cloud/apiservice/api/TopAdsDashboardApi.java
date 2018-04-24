package com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by nakama on 24/04/18.
 */

public interface TopAdsDashboardApi {

    @GET(TopAdsCommonConstant.PATH_TOPADS_TOTAL_ADS)
    Observable<Response<DataResponse<TotalAd>>> populateTotalAd(@QueryMap Map<String, String> params);
}
