package com.tokopedia.tkpd.thankyou.data.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.tokopedia.core.analytics.PurchaseTracking;

import java.util.Map;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 12/7/17.
 */

public class DigitalTrackerMapper implements Func1<Response<String>, Boolean> {
    private static final String DATA = "data";
    private Gson gson;

    public DigitalTrackerMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Boolean call(Response<String> response) {
        JsonObject responseObject = new JsonParser().parse(response.body()).getAsJsonObject();
        PurchaseTracking.digital(PurchaseTracking.TRANSACTION, getMappedData(responseObject.get(DATA)));
        return false;
    }

    private Map<String, Object> getMappedData(JsonElement data) {
        LinkedTreeMap map = gson.fromJson(gson.toJson(data), LinkedTreeMap.class);
        return map;
    }
}
