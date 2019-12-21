package com.tokopedia.home.beranda.presentation.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.List;
import java.util.Map;

public interface HomeVisitable extends Visitable<HomeTypeFactory> {
    void setTrackingData(Map<String, Object> trackingData);
    Map<String, Object> getTrackingData();
    List<Object> getTrackingDataForCombination();
    void setTrackingDataForCombination(List<Object> object);
    boolean isTrackingCombined();
    void setTrackingCombined(boolean isCombined);
    boolean isCache();
    String visitableId();
}
