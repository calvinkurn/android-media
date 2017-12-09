package com.tokopedia.tkpd.thankyou.data.mapper;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.tokopedia.core.analytics.PurchaseTracking;
import com.tokopedia.tkpd.thankyou.data.pojo.digital.DigitalDataWrapper;
import com.tokopedia.tkpd.thankyou.data.pojo.digital.DigitalTrackerData;

import java.util.Map;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 12/7/17.
 */

public class DigitalTrackerMapper implements Func1<Response<DigitalDataWrapper<DigitalTrackerData>>, Boolean> {
    private Gson gson;

    public DigitalTrackerMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Boolean call(Response<DigitalDataWrapper<DigitalTrackerData>> response) {
        PurchaseTracking.digital("transaction", getMappedData(response.body().getData()));
        return false;
    }

    private Map<String, Object> getMappedData(DigitalTrackerData data) {
        LinkedTreeMap map = gson.fromJson(gson.toJson(data), LinkedTreeMap.class);
        return map;
    }
}
