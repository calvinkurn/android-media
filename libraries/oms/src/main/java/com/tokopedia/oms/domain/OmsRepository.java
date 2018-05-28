package com.tokopedia.oms.domain;

import com.google.gson.JsonObject;
import com.tokopedia.oms.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyMyCartResponse;

import rx.Observable;

public interface OmsRepository {

    Observable<VerifyMyCartResponse> verifyCard(JsonObject requestBody);

    Observable<CheckoutResponse> checkoutCart(JsonObject requestBody);

}
