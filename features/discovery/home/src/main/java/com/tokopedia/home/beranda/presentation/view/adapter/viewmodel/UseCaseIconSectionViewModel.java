package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.presentation.view.adapter.TrackedVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class UseCaseIconSectionViewModel implements TrackedVisitable<HomeTypeFactory> {

    private List<HomeIconItem> itemList;
    private Map<String, Object> trackingData;
    private boolean isCombined;
    private List<Object> trackingDataForCombination;

    public UseCaseIconSectionViewModel() {
        itemList = new ArrayList<>();
    }

    public List<HomeIconItem> getItemList() {
        return itemList;
    }

    public void addItem(HomeIconItem item){
        this.itemList.add(item);
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
