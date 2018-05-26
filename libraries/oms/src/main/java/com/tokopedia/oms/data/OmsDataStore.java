package com.tokopedia.oms.data;

import com.google.gson.JsonObject;
import com.tokopedia.oms.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyCartResponse;

import rx.Observable;

public interface OmsDataStore {


    Observable<VerifyCartResponse> verifyCart(JsonObject requestBody);

    Observable<CheckoutResponse> checkoutCart(JsonObject requestBody);

}
