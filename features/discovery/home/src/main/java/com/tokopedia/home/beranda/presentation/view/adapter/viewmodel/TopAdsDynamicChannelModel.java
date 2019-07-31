package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.home.beranda.presentation.view.adapter.TrackedVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;
import com.tokopedia.topads.sdk.base.adapter.Item;

import java.util.List;
import java.util.Map;

/**
 * Author errysuprayogi on 30,November,2018
 */
public class TopAdsDynamicChannelModel implements TrackedVisitable<HomeTypeFactory> {
    private String title;
    private List<Item> items;
    private Map<String, Object> trackingData;
    private boolean isCombined;
    private List<Object> trackingDataForCombination;

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
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
