package com.tokopedia.shop.score.common.plt

import android.content.Context

interface ShopPerformanceMonitoringContract {
    fun stopPreparePerformancePageMonitoring()
    fun startNetworkRequestPerformanceMonitoring()
    fun stopNetworkRequestPerformanceMonitoring()
    fun startRenderPerformanceMonitoring()
    fun castContextToTalkPerformanceMonitoringListener(context: Context) : ShopScorePerformanceMonitoringListener?
}