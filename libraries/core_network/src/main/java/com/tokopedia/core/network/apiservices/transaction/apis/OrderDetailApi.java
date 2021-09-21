package com.tokopedia.core.network.apiservices.transaction.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by kris on 11/9/17. Tokopedia
 */

@Deprecated
public interface OrderDetailApi {

    @GET(TkpdBaseURL.Purchase.PATH_ORDER_HISTORY)
    Call<String> getOrderHistory(@QueryMap Map<String, Object> requestOrderHistoryParams);
}
