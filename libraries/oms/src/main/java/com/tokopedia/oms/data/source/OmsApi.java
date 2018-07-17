package com.tokopedia.oms.data.source;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.oms.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyMyCartResponse;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface OmsApi {

    @POST(OmsUrl.HelperUrl.OMS_VERIFY)
    Observable<Response<DataResponse<VerifyMyCartResponse>>> postCartVerify(@Body JsonObject requestBody);


    @POST(OmsUrl.HelperUrl.OMS_CHECKOUT)
    Observable<Response<DataResponse<JsonObject>>> checkoutCart(@Body JsonObject requestBody, @Query("client") String client, @Query("version") String version);

}
