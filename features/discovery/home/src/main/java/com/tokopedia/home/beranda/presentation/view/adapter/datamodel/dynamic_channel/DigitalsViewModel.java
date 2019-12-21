package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel;

import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.List;
import java.util.Map;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class DigitalsViewModel implements HomeVisitable {

    private String title;
    private int sectionId;
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
        return String.valueOf(sectionId);
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }

    public DigitalsViewModel(String title, int sectionId) {
        this.title = title;
        this.sectionId = sectionId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
