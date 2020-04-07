package com.tokopedia.navigation.analytics.performance;

import java.util.Map;

public class HomePerformanceData extends PerformanceData {
    private Boolean dataLocked = false;
    private Map<String, Integer> dynamicChannelList;

    public HomePerformanceData(String allFramesTag, String jankyFramesTag, String jankyFramesPercentageTag) {
        super(allFramesTag, jankyFramesTag, jankyFramesPercentageTag);
    }

    public Boolean isDataLocked() {
        return dataLocked;
    }

    public void lockData() {
        this.dataLocked = true;
    }

    public Map<String, Integer> getDynamicChannelList() {
        return dynamicChannelList;
    }

    public void setDynamicChannelList(Map<String, Integer> dynamicChannelList) {
        this.dynamicChannelList = dynamicChannelList;
    }
}
