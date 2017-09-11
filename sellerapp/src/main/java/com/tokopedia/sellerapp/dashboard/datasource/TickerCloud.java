package com.tokopedia.sellerapp.dashboard.datasource;

import com.tokopedia.sellerapp.home.api.TickerApiSeller;
import com.tokopedia.sellerapp.home.model.Ticker;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by hendry on 9/11/2017.
 */

public class TickerCloud {
    private final TickerApiSeller api;

    @Inject
    public TickerCloud(TickerApiSeller api) {
        this.api = api;
    }

    public Observable<Response<Ticker>> getTicker(String userId) {
        return api.getTickers(
                userId,
                TickerApiSeller.size,
                TickerApiSeller.FILTER_SELLERAPP_ANDROID_DEVICE);
    }
}
