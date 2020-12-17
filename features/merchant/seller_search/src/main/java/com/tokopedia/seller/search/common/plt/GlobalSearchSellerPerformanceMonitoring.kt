package com.tokopedia.seller.search.common.plt

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback

class GlobalSearchSellerPerformanceMonitoring(private val type: GlobalSearchSellerPerformanceMonitoringType) : GlobalSearchSellerPerformanceMonitoringInterface {

    private var globalSearchPerformanceMonitoring: PageLoadTimePerformanceCallback? = null

    override fun initGlobalSearchSellerPerformanceMonitoring() {
        globalSearchPerformanceMonitoring = PageLoadTimePerformanceCallback(
                type.prepareMetric,
                type.networkMetric,
                type.renderMetric
        ).apply {
            startMonitoring(type.trace)
            startPreparePagePerformanceMonitoring()
        }
    }

    override fun startNetworkGlobalSearchSellerPerformanceMonitoring() {
        globalSearchPerformanceMonitoring?.run {
            if (!isPrepareDone) {
                stopPreparePagePerformanceMonitoring()
                startNetworkRequestPerformanceMonitoring()
            }
        }
    }

    override fun startRenderGlobalSearchSellerPerformanceMonitoring() {
        globalSearchPerformanceMonitoring?.run {
            if (!isNetworkDone) {
                stopNetworkRequestPerformanceMonitoring()
                startRenderPerformanceMonitoring()
            }
        }
    }

    override fun stopPerformanceMonitoring() {
        globalSearchPerformanceMonitoring?.run {
            if (!isRenderDone) {
                stopRenderPerformanceMonitoring()
                stopMonitoring()
            }
        }
    }
}