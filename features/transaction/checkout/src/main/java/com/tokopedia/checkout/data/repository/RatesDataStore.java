package com.tokopedia.checkout.data.repository;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.data.entity.response.rates.RatesResponse;
import com.tokopedia.checkout.data.service.RatesService;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Irfan Khoirul on 20/02/18.
 */

public class RatesDataStore {
    private final RatesService service;

    @Inject
    public RatesDataStore(RatesService service) {
        this.service = service;
    }

    public Observable<RatesResponse> getRates(TKPDMapParam<String, String> params) {
        return service.getApi().calculateShippingRate(params)
                .map(new Func1<Response<String>, RatesResponse>() {
                    @Override
                    public RatesResponse call(Response<String> stringResponse) {
                        return new Gson().fromJson(stringResponse.body(), RatesResponse.class);
                    }
                });
    }
}
