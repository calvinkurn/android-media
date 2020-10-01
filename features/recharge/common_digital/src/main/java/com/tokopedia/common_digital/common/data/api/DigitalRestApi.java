package com.tokopedia.common_digital.common.data.api;

import com.google.gson.JsonObject;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Rizky on 13/08/18.
 */
public interface DigitalRestApi {

    @POST("ussd/balance")
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> parsePulsaMessage(@Body JsonObject requestBody);

}
