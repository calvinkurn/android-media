package com.tokopedia.search.result.product.performancemonitoring

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*

import org.junit.Test

class SearchProductPerformanceMonitoringKtTest {

    private val performanceMonitoring = mockk<PageLoadTimePerformanceInterface>(relaxed = true)
    private val action = mockk<() -> Unit>(relaxed = true)

    @Test
    fun `runCustomMetric with null performance monitoring will invoke action exactly once`() {
        runCustomMetric(null, TAG, action)

        verify (exactly = 1) { action.invoke() }
    }

    @Test
    fun `runCustomMetric with performance monitoring will wrap action with start and stop custom metrics`() {
        runCustomMetric(performanceMonitoring, TAG, action)

        verifyOrder {
            performanceMonitoring.startCustomMetric(TAG)
            action.invoke()
            performanceMonitoring.stopCustomMetric(TAG)
        }
    }

    @Test
    fun `runCustomMetric with return value from the invoked action`() {
        val returnValue = runCustomMetric(performanceMonitoring, TAG) { 1 + 2 }

        assertThat(returnValue, `is`(3))
    }

    companion object {
        private const val TAG = "performance_monitoring_tag"
    }
}