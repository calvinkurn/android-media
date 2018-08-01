package com.tokopedia.oms.domain;

import com.google.gson.JsonObject;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyMyCartResponse;

import rx.Observable;

public interface OmsRepository {

    Observable<VerifyMyCartResponse> verifyCard(JsonObject requestBody, boolean flag);

    Observable<JsonObject> checkoutCart(JsonObject requestBody);

}
