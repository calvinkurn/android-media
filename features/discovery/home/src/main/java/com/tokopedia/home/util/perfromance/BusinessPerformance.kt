package com.tokopedia.home.util.perfromance

import com.tokopedia.analytics.performance.PerformanceMonitoring

object BusinessWidgetPerformance {
    private var performanceMonitoring: PerformanceMonitoring?=null

    fun start(){
        if(performanceMonitoring == null){
            performanceMonitoring = PerformanceMonitoring.start("new_business_widget_render_time")
        }
    }

    fun stop(){
        if(performanceMonitoring != null){
            performanceMonitoring?.stopTrace()
            performanceMonitoring = null
        }
    }
}