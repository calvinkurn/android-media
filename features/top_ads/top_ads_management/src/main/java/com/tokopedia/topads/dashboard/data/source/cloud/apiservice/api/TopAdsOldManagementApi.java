package com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api;

import com.tokopedia.product.manage.item.common.data.source.cloud.DataResponse;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by zulfikarrahman on 11/4/16.
 */
public interface TopAdsOldManagementApi {

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_DEPOSIT)
    Observable<Response<DataResponse<DataDeposit>>> getDashboardDeposit(@QueryMap Map<String, String> params);

}