package com.tokopedia.review.common.analytics

import android.content.Context

interface ReviewPerformanceMonitoringContract {
    fun stopPreparePerfomancePageMonitoring()
    fun startNetworkRequestPerformanceMonitoring()
    fun stopNetworkRequestPerformanceMonitoring()
    fun startRenderPerformanceMonitoring()
    fun castContextToTalkPerformanceMonitoringListener(context: Context) : ReviewPerformanceMonitoringListener?
}