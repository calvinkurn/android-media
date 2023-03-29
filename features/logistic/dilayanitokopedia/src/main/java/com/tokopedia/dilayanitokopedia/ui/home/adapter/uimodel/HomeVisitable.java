package com.tokopedia.dilayanitokopedia.ui.home.adapter.uimodel;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.dilayanitokopedia.common.view.adapter.typefactory.HomeTypeFactory;

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

    boolean equalsWith(Object b);

    Bundle getChangePayloadFrom(Object b);
}
