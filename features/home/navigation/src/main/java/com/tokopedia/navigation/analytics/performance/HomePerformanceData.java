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

    public void setDataLocked(Boolean dataLocked) {
        this.dataLocked = dataLocked;
    }

    public Map<String, Integer> getDynamicChannelList() {
        return dynamicChannelList;
    }

    public void setDynamicChannelList(Map<String, Integer> dynamicChannelList) {
        this.dynamicChannelList = dynamicChannelList;
    }
}
