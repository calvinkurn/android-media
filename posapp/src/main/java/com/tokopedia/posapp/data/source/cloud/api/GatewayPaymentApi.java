package com.tokopedia.posapp.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by okasurya on 10/17/17.
 */

public interface GatewayPaymentApi {
    String CONTENT_TYPE_APPLICATION_JSON = "Content-Type: application/json";

    @POST(TkpdBaseURL.Pos.CREATE_ORDER)
    @Headers(CONTENT_TYPE_APPLICATION_JSON)
    Observable<Response<TkpdResponse>> createOrder(@Body String json);

    @GET(TkpdBaseURL.Pos.GET_PAYMENT_STATUS)
    Observable<Response<TkpdResponse>> getPaymentStatus(@QueryMap Map<String, String> param);
}
