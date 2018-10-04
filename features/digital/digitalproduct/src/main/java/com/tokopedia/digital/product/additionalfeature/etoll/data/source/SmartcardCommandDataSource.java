package com.tokopedia.digital.product.additionalfeature.etoll.data.source;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardcommand.RequestBodySmartcardCommand;
import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.response.ResponseSmartcard;
import com.tokopedia.digital.product.additionalfeature.etoll.data.mapper.SmartcardMapper;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 18/05/18.
 */
public class SmartcardCommandDataSource {

    private DigitalEndpointService digitalEndpointService;
    private SmartcardMapper smartcardMapper;

    public SmartcardCommandDataSource(DigitalEndpointService digitalEndpointService,
                                      SmartcardMapper smartcardMapper) {
        this.digitalEndpointService = digitalEndpointService;
        this.smartcardMapper = smartcardMapper;
    }

    public Observable<InquiryBalanceModel> sendCommand(RequestBodySmartcardCommand requestBodySmartcardCommand) {
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestBodySmartcardCommand));
        final JsonObject requestBody = new JsonObject();
        requestBody.add("data", jsonElement);

        return digitalEndpointService.getApi().smartcardCommand(requestBody)
                .map(new Func1<Response<TkpdDigitalResponse>, InquiryBalanceModel>() {
                    @Override
                    public InquiryBalanceModel call(Response<TkpdDigitalResponse> response) {
                        return smartcardMapper.map(response.body().convertDataObj(ResponseSmartcard.class));
                    }
                });
    }

}
