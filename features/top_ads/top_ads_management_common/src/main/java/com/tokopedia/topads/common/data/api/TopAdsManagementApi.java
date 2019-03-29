package com.tokopedia.topads.common.data.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.topads.common.data.model.DataCheckPromo;
import com.tokopedia.topads.common.data.model.DataDeposit;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by hadi.putra on 23/04/18.
 */

public interface TopAdsManagementApi {

    @GET(TopAdsCommonConstant.PATH_TOPADS_SHOP_DEPOSIT)
    Observable<Response<DataResponse<DataDeposit>>> getDashboardDeposit(@QueryMap Map<String, String> params);

    @GET(TopAdsCommonConstant.PATH_CHECK_PROMO)
    Observable<Response<DataResponse<DataCheckPromo>>> checkPromoAds(@QueryMap Map<String, String> params);
}
