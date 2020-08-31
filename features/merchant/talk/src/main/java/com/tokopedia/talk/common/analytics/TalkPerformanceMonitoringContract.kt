package com.tokopedia.talk.common.analytics

import android.content.Context

interface TalkPerformanceMonitoringContract {
    fun stopPreparePerfomancePageMonitoring()
    fun startNetworkRequestPerformanceMonitoring()
    fun stopNetworkRequestPerformanceMonitoring()
    fun startRenderPerformanceMonitoring()
    fun castContextToTalkPerformanceMonitoringListener(context: Context) : TalkPerformanceMonitoringListener?
}