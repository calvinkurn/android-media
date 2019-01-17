package com.tokopedia.digital.product.data.repository;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.common_digital.common.data.api.DigitalRestApi;
import com.tokopedia.digital.product.data.entity.requestbody.pulsabalance.RequestBodyPulsaBalance;
import com.tokopedia.digital.product.data.entity.response.ResponsePulsaBalance;
import com.tokopedia.digital.product.data.mapper.USSDMapper;
import com.tokopedia.digital.product.domain.IUssdCheckBalanceRepository;
import com.tokopedia.digital.product.view.model.PulsaBalance;

import rx.Observable;

/**
 * Created by ashwanityagi on 04/07/17.
 */

public class UssdCheckBalanceRepository implements IUssdCheckBalanceRepository {

    private final DigitalRestApi digitalRestApi;
    private final USSDMapper ussdMapper;

    public UssdCheckBalanceRepository(DigitalRestApi digitalRestApi,
                                      USSDMapper ussdMapper) {
        this.digitalRestApi = digitalRestApi;
        this.ussdMapper = ussdMapper;
    }

    @Override
    public Observable<PulsaBalance> processPulsaBalanceUssdResponse(RequestBodyPulsaBalance requestBodyPulsaBalance) {
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestBodyPulsaBalance));
        JsonObject requestBody = new JsonObject();
        requestBody.add("data", jsonElement);
        return digitalRestApi.parsePulsaMessage(requestBody)
                .map(tkpdDigitalResponseResponse -> {
                    ResponsePulsaBalance responsePulsaBalance =
                            tkpdDigitalResponseResponse.body().convertDataObj(ResponsePulsaBalance.class);
                    return ussdMapper.transformPulsaBalance(responsePulsaBalance);
                });
    }

}