package com.tokopedia.oms.data;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.network.mapper.DataResponseMapper;
import com.tokopedia.oms.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyMyCartResponse;
import com.tokopedia.oms.data.source.OmsApi;

import javax.inject.Inject;

import rx.Observable;



public class CloudOmsDataStore implements OmsDataStore {

    private final OmsApi omsApi;

    @Inject
    public CloudOmsDataStore(OmsApi eventsApi) {
        this.omsApi = eventsApi;
    }

    @Override
    public Observable<VerifyMyCartResponse> verifyCart(JsonObject requestBody) {
        return omsApi.postCartVerify(requestBody).map(new DataResponseMapper<VerifyMyCartResponse>());
    }

    @Override
    public Observable<CheckoutResponse> checkoutCart(JsonObject requestBody) {
        return omsApi.checkoutCart(requestBody).map(new DataResponseMapper<CheckoutResponse>());
    }

}
