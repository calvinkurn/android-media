package com.tokopedia.core.common.ticker.datasource;

import com.tokopedia.core.common.ticker.model.Ticker;
import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by User on 9/11/2017.
 */

public class TickerDataSource {
    private final TickerCloud tickerCloud;

    @Inject
    public TickerDataSource(TickerCloud tickerCloud) {
        this.tickerCloud = tickerCloud;
    }

    public Observable<Response<Ticker>> getTicker(String userId) {
        return tickerCloud.getTicker(userId);
    }
}
