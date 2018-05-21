package com.tokopedia.digital_deals.data.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tokopedia.digital_deals.data.entity.response.dealdetailsresponse.DealDetailsResponse;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomailmodel.DealsDetailsDomain;

import rx.functions.Func1;

public class DealDetailsTransformMapper implements Func1<DealDetailsResponse, DealsDetailsDomain>{
    @Override
    public DealsDetailsDomain call(DealDetailsResponse dealDetailsResponse) {

        JsonObject detail = dealDetailsResponse.getData();

        return new Gson().fromJson(detail, DealsDetailsDomain.class);
    }
}
