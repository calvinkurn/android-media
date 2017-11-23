package com.tokopedia.core.common.ticker.repository;

import com.tokopedia.core.common.mapper.SimpleResponseMapper;
import com.tokopedia.core.common.ticker.datasource.TickerDataSource;
import com.tokopedia.core.common.ticker.model.Ticker;
import com.tokopedia.core.util.SessionHandler;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by User on 9/11/2017.
 */

public class TickerRepository {
    private final TickerDataSource tickerDataSource;
    private SessionHandler sessionHandler;

    @Inject
    public TickerRepository(TickerDataSource tickerDataSource, SessionHandler sessionHandler) {
        this.tickerDataSource = tickerDataSource;
        this.sessionHandler = sessionHandler;
    }

    public Observable<Ticker.Tickers[]> getTicker() {
        String userId = sessionHandler.getLoginID();
        return tickerDataSource.getTicker(userId).map(new SimpleResponseMapper<Ticker>())
                .map(new Func1<Ticker, Ticker.Tickers[]>() {
            @Override
            public Ticker.Tickers[] call(Ticker ticker) {
                return ticker.getData().getTickers();
            }
        });
    }
}