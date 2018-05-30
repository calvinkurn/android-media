package com.tokopedia.oms.data;

import com.google.gson.JsonObject;
import com.tokopedia.oms.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyMyCartResponse;
import com.tokopedia.oms.domain.OmsRepository;

import javax.inject.Inject;

import rx.Observable;


public class OmsRepositoryData implements OmsRepository {

    private CloudOmsDataStore mCloudOmsDataStore;

    @Inject
    public OmsRepositoryData(CloudOmsDataStore cloudOmsDataStore) {
        this.mCloudOmsDataStore = cloudOmsDataStore;

    }

    @Override
    public Observable<VerifyMyCartResponse> verifyCard(JsonObject requestBody) {
        return mCloudOmsDataStore.verifyCart(requestBody);
    }

    @Override
    public Observable<CheckoutResponse> checkoutCart(JsonObject requestBody) {
        return mCloudOmsDataStore.checkoutCart(requestBody);
    }
}
