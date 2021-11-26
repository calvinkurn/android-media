package com.tokopedia.navigation_common.listener;

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface;

public interface OfficialStorePerformanceMonitoringListener {
    void stopOfficialStorePerformanceMonitoring(boolean isCache);
    void startOfficialStorePerformanceMonitoring();
    PageLoadTimePerformanceInterface getOfficialStorePageLoadTimePerformanceInterface();
}
