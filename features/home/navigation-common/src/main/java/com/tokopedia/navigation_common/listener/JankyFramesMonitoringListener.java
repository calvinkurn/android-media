package com.tokopedia.navigation_common.listener;

import java.util.Map;

/**
 * @author : Fikry 03/12/19
 */
public interface JankyFramesMonitoringListener {
    void submitDynamicChannelCount(Map<String, Integer> dynamicChannelList);
    Boolean needToSubmitDynamicChannelCount();
}
