package com.tokopedia.digital_deals.data.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomailmodel.DealsDetailsDomain;

import rx.functions.Func1;

public class DealDetailsTransformMapper implements Func1<JsonObject, DealsDetailsDomain>{
    @Override
    public DealsDetailsDomain call(JsonObject dealDetailsResponse) {

        return new Gson().fromJson(dealDetailsResponse, DealsDetailsDomain.class);
    }
}
