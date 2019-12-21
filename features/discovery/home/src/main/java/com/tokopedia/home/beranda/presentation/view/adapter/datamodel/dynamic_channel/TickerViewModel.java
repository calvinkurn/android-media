package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel;

import com.tokopedia.home.beranda.domain.model.Ticker;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class TickerViewModel implements HomeVisitable {

    private ArrayList<Ticker.Tickers> tickers;
    private boolean isCache;
    private Map<String, Object> trackingData;
    private List<Object> trackingDataForCombination;
    private boolean isCombined;

    @Override
    public boolean isCache() {
        return isCache;
    }

    @Override
    public String visitableId() {
        return "hometicker";
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }


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
