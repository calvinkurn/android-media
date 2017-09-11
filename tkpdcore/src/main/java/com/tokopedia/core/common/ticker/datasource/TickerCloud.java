package com.tokopedia.core.common.ticker.datasource;

import com.tokopedia.core.common.ticker.api.TickerApiSeller;
import com.tokopedia.core.common.ticker.model.Ticker;

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
