package com.tokopedia.logisticdata.data.repository;

import com.google.gson.Gson;
import com.tokopedia.logisticdata.data.apiservice.RatesApi;
import com.tokopedia.logisticdata.data.entity.rates.RatesResponse;

import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Irfan Khoirul on 20/02/18.
 */

public class RatesRepository {

    private final RatesApi ratesApi;

    @Inject
    public RatesRepository(RatesApi ratesApi) {
        this.ratesApi = ratesApi;
    }

    public Observable<RatesResponse> getRates(Map<String, String> parameters) {
        return ratesApi.calculateShippingRate(parameters)
                .map(new Func1<Response<String>, RatesResponse>() {
                    @Override
                    public RatesResponse call(Response<String> stringResponse) {
                        return new Gson().fromJson(stringResponse.body(), RatesResponse.class);
                    }
                });
    }
}
