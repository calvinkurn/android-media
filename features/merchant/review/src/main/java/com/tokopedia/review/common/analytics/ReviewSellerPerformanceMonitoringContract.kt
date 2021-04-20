package com.tokopedia.review.common.analytics

import android.content.Context

interface ReviewSellerPerformanceMonitoringContract {
    fun stopPreparePerformancePageMonitoring()
    fun startNetworkRequestPerformanceMonitoring()
    fun stopNetworkRequestPerformanceMonitoring()
    fun startRenderPerformanceMonitoring()
    fun castContextToTalkPerformanceMonitoringListener(context: Context) : ReviewSellerPerformanceMonitoringListener?
}