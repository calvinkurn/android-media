package com.tokopedia.oms.data;

import com.google.gson.JsonObject;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyMyCartResponse;

import rx.Observable;

public interface OmsDataStore {


    Observable<VerifyMyCartResponse> verifyCart(JsonObject requestBody, boolean flag);

    Observable<JsonObject> checkoutCart(JsonObject requestBody);

}
