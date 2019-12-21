package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel;

import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.HomeIconItem;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author by errysuprayogi on 11/28/17.
 */

/**
 * No further development for this datamodel
 * Use dynamic icon section instead
 */

@Deprecated
public class UseCaseIconSectionViewModel implements HomeVisitable {

    private List<HomeIconItem> itemList;
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
        return "";
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }

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
