package com.tokopedia.logisticCommon.data.apiservice;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.logisticCommon.data.constant.LogisticDataConstantUrl;

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
    Observable<Response<TokopediaWsV4Response>> trackOrder(@QueryMap Map<String, String> params);

}
