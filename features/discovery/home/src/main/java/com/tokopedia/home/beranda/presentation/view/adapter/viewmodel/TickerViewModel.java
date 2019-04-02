package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.domain.model.Ticker;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.ArrayList;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class TickerViewModel implements Visitable<HomeTypeFactory> {

    private ArrayList<Ticker.Tickers> tickers;

    public ArrayList<Ticker.Tickers> getTickers() {
        return tickers;
    }

    public void setTickers(ArrayList<Ticker.Tickers> tickers) {
        this.tickers = tickers;
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
