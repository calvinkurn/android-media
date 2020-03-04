package com.tokopedia.home.util.performance

import com.tokopedia.analytics.performance.PerformanceMonitoring

object BusinessWidgetPerformance {
    private val performanceMonitoring = PerformanceMonitoring.start("business_widget_render_time")

    fun start(){
        performanceMonitoring.startTrace("business_widget_render_time")
    }

    fun stop(){
        performanceMonitoring.stopTrace()
    }
}