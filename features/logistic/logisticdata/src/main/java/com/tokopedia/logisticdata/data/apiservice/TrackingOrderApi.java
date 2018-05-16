package com.tokopedia.logisticdata.data.apiservice;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.logisticdata.data.constant.LogisticDataConstantUrl;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by kris on 5/14/18. Tokopedia
 */

public interface TrackingOrderApi {

    @GET(LogisticDataConstantUrl.CourierTracking.PATH_TRACK_ORDER)
    Observable<Response<TkpdResponse>> trackOrder(@QueryMap Map<String, String> params);

}
