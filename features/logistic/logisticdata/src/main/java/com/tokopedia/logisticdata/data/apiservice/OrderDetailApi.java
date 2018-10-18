package com.tokopedia.logisticdata.data.apiservice;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.network.constant.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface OrderDetailApi {

    @GET(TkpdBaseURL.Purchase.PATH_ORDER_DETAIL)
    Observable<Response<TokopediaWsV4Response>> getOrderDetail(@QueryMap Map<String, Object> requestOrderDetailParams);

    @GET(TkpdBaseURL.Purchase.PATH_ORDER_HISTORY)
    Observable<Response<String>> getOrderHistory(@QueryMap Map<String, Object> requestOrderHistoryParams);

    @GET(TkpdBaseURL.Purchase.PATH_CHANGE_COURIER)
    Observable<Response<TokopediaWsV4Response>> changeCourier(@QueryMap Map<String, String> requestOrderHistoryParams);
}

