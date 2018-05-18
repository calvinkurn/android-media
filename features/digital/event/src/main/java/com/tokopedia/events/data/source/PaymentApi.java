package com.tokopedia.events.data.source;

import com.google.gson.JsonObject;
import com.tokopedia.events.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by pranaymohapatra on 10/05/18.
 */

public interface PaymentApi {

    @POST(EventsUrl.EVENTS_CHECKOUT)
    @Headers({"Content-Type: application/json"})
    Observable<CheckoutResponse> checkoutCart(@Body JsonObject requestBody);

    @POST(EventsUrl.EVENTS_VERIFY)
    @Headers({"Content-Type: application/json"})
    Observable<VerifyCartResponse> postCartVerify(@Body JsonObject requestBody, @Query("book") boolean value);
}
