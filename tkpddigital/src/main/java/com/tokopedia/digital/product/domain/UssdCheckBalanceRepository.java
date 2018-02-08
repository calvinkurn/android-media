package com.tokopedia.digital.product.domain;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.digital.apiservice.DigitalEndpointService;
import com.tokopedia.digital.product.data.entity.requestbody.pulsabalance.RequestBodyPulsaBalance;
import com.tokopedia.digital.product.data.entity.response.ResponsePulsaBalance;
import com.tokopedia.digital.product.data.mapper.IProductDigitalMapper;
import com.tokopedia.digital.product.model.PulsaBalance;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ashwanityagi on 04/07/17.
 */

public class UssdCheckBalanceRepository implements IUssdCheckBalanceRepository {
    private final DigitalEndpointService digitalEndpointService;
    private final IProductDigitalMapper productDigitalMapper;

    public UssdCheckBalanceRepository(DigitalEndpointService digitalEndpointService,
                                      IProductDigitalMapper productDigitalMapper) {
        this.digitalEndpointService = digitalEndpointService;
        this.productDigitalMapper = productDigitalMapper;
    }

    @Override
    public Observable<PulsaBalance> processPulsaBalanceUssdResponse(RequestBodyPulsaBalance requestBodyPulsaBalance) {
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestBodyPulsaBalance));
        JsonObject requestBody = new JsonObject();
        requestBody.add("data", jsonElement);
        return digitalEndpointService.getApi().parsePulsaMessage(requestBody)
                .map(new Func1<Response<TkpdDigitalResponse>, PulsaBalance>() {
                    @Override
                    public PulsaBalance call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        ResponsePulsaBalance responsePulsaBalance =tkpdDigitalResponseResponse.body().convertDataObj(ResponsePulsaBalance.class);
                        return productDigitalMapper.transformPulsaBalance(responsePulsaBalance);
                    }
                });
    }
}