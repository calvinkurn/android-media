package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.domain.model.Ticker;
import com.tokopedia.home.beranda.presentation.view.adapter.TrackedVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class TickerViewModel implements TrackedVisitable<HomeTypeFactory> {

    private ArrayList<Ticker.Tickers> tickers;
    private Map<String, Object> trackingData;
    private boolean isCombined;
    private List<Object> trackingDataForCombination;

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

    @Override
    public void setTrackingData(Map<String, Object> trackingData) {
        this.trackingData = trackingData;
    }

    @Override
    public Map<String, Object> getTrackingData() {
        return trackingData;
    }

    @Override
    public List<Object> getTrackingDataForCombination() {
        return trackingDataForCombination;
    }

    @Override
    public void setTrackingDataForCombination(List<Object> trackingDataForCombination) {
        this.trackingDataForCombination = trackingDataForCombination;
    }

    @Override
    public boolean isTrackingCombined() {
        return isCombined;
    }

    @Override
    public void setTrackingCombined(boolean isCombined) {
        this.isCombined = isCombined;
    }
}
