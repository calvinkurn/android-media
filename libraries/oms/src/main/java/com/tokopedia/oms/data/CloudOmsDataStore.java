package com.tokopedia.oms.data;

import com.google.gson.JsonObject;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.network.mapper.DataResponseMapper;
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
    public Observable<VerifyMyCartResponse> verifyCart(JsonObject requestBody, boolean flag) {
        return omsApi.postCartVerify(requestBody, flag).map(new DataResponseMapper<>());
    }

    @Override
    public Observable<JsonObject> checkoutCart(JsonObject requestBody) {
        String appversion = GlobalConfig.VERSION_NAME;
        String client = "android";
        return omsApi.checkoutCart(requestBody, client, appversion).map(new DataResponseMapper<>());
    }

}
