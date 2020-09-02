package com.tokopedia.vouchercreation.common.plt

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback

class MvcPerformanceMonitoring(private val type: MvcPerformanceMonitoringType) : MvcPerformanceMonitoringInterface {

    private var mvcPerformanceMonitoring: PageLoadTimePerformanceCallback? = null

    override fun initMvcPerformanceMonitoring() {
        mvcPerformanceMonitoring = PageLoadTimePerformanceCallback(
                type.prepareMetric,
                type.networkMetric,
                type.renderMetric
        ).apply {
            startMonitoring(type.trace)
            startPreparePagePerformanceMonitoring()
        }
    }

    override fun startNetworkMvcPerformanceMonitoring() {
        mvcPerformanceMonitoring?.run {
            if (!isPrepareDone) {
                stopPreparePagePerformanceMonitoring()
                startNetworkRequestPerformanceMonitoring()
            }
        }
    }

    override fun startRenderMvcPerformanceMonitoring() {
        mvcPerformanceMonitoring?.run {
            if (!isNetworkDone) {
                stopNetworkRequestPerformanceMonitoring()
                startRenderPerformanceMonitoring()
            }
        }
    }

    override fun stopPerformanceMonitoring() {
        mvcPerformanceMonitoring?.run {
            if (!isRenderDone) {
                stopRenderPerformanceMonitoring()
                stopMonitoring()
            }
        }
    }
    
}