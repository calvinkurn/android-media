package com.tokopedia.navigation_common.listener;

import com.tokopedia.analytics.performance.util.JankyFrameMonitoringUtil;

import java.util.Map;

/**
 * @author : Fikry 03/12/19
 */
public interface JankyFramesMonitoringListener {
    JankyFrameMonitoringUtil getMainJankyFrameMonitoringUtil();
}
