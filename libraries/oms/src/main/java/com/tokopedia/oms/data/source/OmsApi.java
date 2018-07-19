package com.tokopedia.oms.data.source;

import com.google.gson.JsonObject;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyMyCartResponse;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface OmsApi {

    @POST(OmsUrl.OMS_VERIFY)
    @Headers({"Content-Type: application/json"})
    Observable<VerifyMyCartResponse> postCartVerify(@Body JsonObject requestBody, @Query("book") boolean value);


    @POST(OmsUrl.OMS_CHECKOUT)
    @Headers({"Content-Type: application/json"})
    Observable<JsonObject> checkoutCart(@Body JsonObject requestBody, @Query("client") String client, @Query("version") String version);

}
