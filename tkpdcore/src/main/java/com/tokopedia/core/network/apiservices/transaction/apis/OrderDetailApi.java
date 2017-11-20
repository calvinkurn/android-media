package com.tokopedia.core.network.apiservices.transaction.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by kris on 11/9/17. Tokopedia
 */

public interface OrderDetailApi {

    @GET(TkpdBaseURL.Purchase.PATH_ORDER_DETAIL)
    Observable<Response<String>> getOrderDetail(@QueryMap Map<String, Object> requestOrderDetailParams);

    @GET(TkpdBaseURL.Purchase.PATH_ORDER_HISTORY)
    Observable<Response<String>> getOrderHistory(@QueryMap Map<String, Object> requestOrderHistoryParams);

}
