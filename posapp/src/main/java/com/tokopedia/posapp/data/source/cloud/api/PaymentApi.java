package com.tokopedia.posapp.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by okasurya on 10/11/17.
 */

public interface PaymentApi {
    String CONTENT_TYPE_APPLICATION_JSON = "Content-Type: application/json";

    @POST(TkpdBaseURL.Payment.PATH_O2O_PAYMENT_ACTION)
    @Headers(CONTENT_TYPE_APPLICATION_JSON)
    Observable<Response<TkpdResponse>> createOrder(@Body String json);

}
