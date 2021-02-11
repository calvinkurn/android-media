package com.tokopedia.statistic.analytics.performance

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PltPerformanceData

/**
 * Created By @ilhamsuaib on 27/08/20
 */

class StatisticPerformanceMonitoring : StatisticPerformanceMonitoringInterface {

    companion object {
        const val STATISTIC_TRACE_NAME = "statistic_trace"
        const val STATISTIC_PREPARE_METRICS = "statistic_plt_prepare_metrics"
        const val STATISTIC_NETWORK_METRICS = "statistic_plt_network_metrics"
        const val STATISTIC_RENDER_METRICS = "statistic_plt_render_metrics"
    }

    private var performanceMonitoring: PageLoadTimePerformanceCallback? = null

    override fun initPerformanceMonitoring() {
        performanceMonitoring = PageLoadTimePerformanceCallback(
                tagPrepareDuration = STATISTIC_PREPARE_METRICS,
                tagNetworkRequestDuration = STATISTIC_NETWORK_METRICS,
                tagRenderDuration = STATISTIC_RENDER_METRICS
        )
        performanceMonitoring?.startMonitoring(STATISTIC_TRACE_NAME)
        performanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun startNetworkPerformanceMonitoring() {
        performanceMonitoring?.run {
            if (!isPrepareDone) {
                stopPreparePagePerformanceMonitoring()
                startNetworkRequestPerformanceMonitoring()
            }
        }
    }

    override fun startRenderPerformanceMonitoring() {
        performanceMonitoring?.run {
            if (!isNetworkDone) {
                stopNetworkRequestPerformanceMonitoring()
                startRenderPerformanceMonitoring()
            }
        }
    }

    override fun stopPerformanceMonitoring() {
        performanceMonitoring?.run {
            if (!isRenderDone) {
                stopRenderPerformanceMonitoring()
                stopMonitoring()
            }
        }
    }

    override fun getPltResult(): PltPerformanceData? {
        return performanceMonitoring?.getPltPerformanceData()
    }
}