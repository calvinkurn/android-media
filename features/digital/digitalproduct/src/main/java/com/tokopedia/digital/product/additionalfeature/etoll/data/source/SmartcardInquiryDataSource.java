package com.tokopedia.digital.product.additionalfeature.etoll.data.source;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.digital.common.data.apiservice.DigitalRestApi;
import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardinquiry.RequestBodySmartcardInquiry;
import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.response.ResponseSmartcard;
import com.tokopedia.digital.product.additionalfeature.etoll.data.mapper.SmartcardMapper;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;

import rx.Observable;

/**
 * Created by Rizky on 18/05/18.
 */
public class SmartcardInquiryDataSource {

    private DigitalRestApi digitalEndpointService;
    private SmartcardMapper smartcardMapper;

    public SmartcardInquiryDataSource(DigitalRestApi digitalEndpointService,
                                      SmartcardMapper smartcardMapper) {
        this.digitalEndpointService = digitalEndpointService;
        this.smartcardMapper = smartcardMapper;
    }

    public Observable<InquiryBalanceModel> inquiryBalance(RequestBodySmartcardInquiry requestBodySmartcardInquiry) {
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestBodySmartcardInquiry));
        final JsonObject requestBody = new JsonObject();
        requestBody.add("data", jsonElement);
        return digitalEndpointService.smartcardInquiry(requestBody)
                .map(response -> smartcardMapper.map(response.body().convertDataObj(ResponseSmartcard.class)));
    }

}
