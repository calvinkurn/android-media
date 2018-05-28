package com.tokopedia.oms.data;

import com.google.gson.JsonObject;
import com.tokopedia.oms.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.oms.data.source.OmsApi;

import rx.Observable;



public class CloudOmsDataStore implements OmsDataStore {

    private final OmsApi omsApi;

    public CloudOmsDataStore(OmsApi eventsApi) {
        this.omsApi = eventsApi;
    }

    @Override
    public Observable<VerifyCartResponse> verifyCart(JsonObject requestBody) {
        return omsApi.postCartVerify(requestBody);
    }

    @Override
    public Observable<CheckoutResponse> checkoutCart(JsonObject requestBody) {
        return omsApi.checkoutCart(requestBody);
    }




}
