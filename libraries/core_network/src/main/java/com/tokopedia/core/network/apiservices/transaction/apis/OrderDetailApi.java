package com.tokopedia.core.network.apiservices.transaction.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by kris on 11/9/17. Tokopedia
 */

@Deprecated
public interface OrderDetailApi {

    @GET(TkpdBaseURL.Purchase.PATH_ORDER_DETAIL)
    Observable<Response<TkpdResponse>> getOrderDetail(@QueryMap Map<String, Object> requestOrderDetailParams);

    @GET(TkpdBaseURL.Purchase.PATH_ORDER_HISTORY)
    Observable<Response<String>> getOrderHistory(@QueryMap Map<String, Object> requestOrderHistoryParams);

    @GET(TkpdBaseURL.Purchase.PATH_CHANGE_COURIER)
    Observable<Response<TkpdResponse>> changeCourier(@QueryMap Map<String, String> requestOrderHistoryParams);
}
