package com.tokopedia.navigation_common.listener;

import com.tokopedia.analytics.performance.perf.BlocksPerformanceTrace;
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface;

/**
 * @author : Fikry 03/12/19
 */
public interface HomePerformanceMonitoringListener {
    void startHomePerformanceMonitoring();
    void stopHomePerformanceMonitoring(boolean isCache);
    PageLoadTimePerformanceInterface getPageLoadTimePerformanceInterface();

    BlocksPerformanceTrace getBlocksPerformanceMonitoring();
}
