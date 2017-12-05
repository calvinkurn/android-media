package com.tokopedia.tkpd.home.thankyou.data.source.api;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by okasurya on 12/5/17.
 */

public interface DigitalThanksApi {
    @POST("v1.4/track/thankyou")
    @Headers({"Content-Type: application/json"})
    Observable<Response<String>> getPaymentData(@Body JsonObject requestBody);
}
