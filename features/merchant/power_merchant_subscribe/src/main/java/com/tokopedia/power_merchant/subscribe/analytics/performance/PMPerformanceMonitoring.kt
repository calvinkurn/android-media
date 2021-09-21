package com.tokopedia.power_merchant.subscribe.analytics.performance

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.active.common.plt.LoadTimeMonitoring

/**
 * Created By @ilhamsuaib on 08/06/21
 */

class PMPerformanceMonitoring : LoadTimeMonitoring() {

    override fun initPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                PerformanceMonitoringConst.POWER_MERCHANT_PREPARE_METRICS,
                PerformanceMonitoringConst.POWER_MERCHANT_NETWORK_METRICS,
                PerformanceMonitoringConst.POWER_MERCHANT_RENDER_METRICS
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(PerformanceMonitoringConst.POWER_MERCHANT_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    fun getCustomMetricsDurationIfCompleted(key: String): Long {
        return (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.let { pltMonitoring ->
            pltMonitoring.customMetric[key].takeIf {
                pltMonitoring.isCustomMetricDone[key] == true
            }
        }.orZero()
    }
}