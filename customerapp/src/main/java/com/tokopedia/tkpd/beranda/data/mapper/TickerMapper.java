package com.tokopedia.tkpd.beranda.data.mapper;

import com.tokopedia.core.network.entity.home.Ticker;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class TickerMapper implements Func1<Response<Ticker>, Ticker>{

    @Override
    public Ticker call(Response<Ticker> tickerResponse) {
        return tickerResponse.body();
    }
}
