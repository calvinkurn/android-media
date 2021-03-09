package com.tokopedia.play.view.monitoring

import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.play.di.PlayScope
import javax.inject.Inject


/**
 * Created by mzennis on 08/03/21.
 */
@PlayScope
class PlayVideoLatencyPerformanceMonitoring @Inject constructor() {

    private val totalDuration: Long
        get() = mTotalDuration

    private var mPerformanceMonitoring: PerformanceMonitoring? = null
    private var mTotalDuration = 0L

    fun start() {
        mTotalDuration = System.currentTimeMillis()
        mPerformanceMonitoring?.startTrace(PLAY_VIDEO_LATENCY_TRACE)
    }

    fun stop() {
        mTotalDuration = System.currentTimeMillis() - mTotalDuration
        mPerformanceMonitoring?.stopTrace()
    }

    fun reset() {
        mPerformanceMonitoring = null
        mTotalDuration = 0L
    }

    companion object {
        private const val PLAY_VIDEO_LATENCY_TRACE = "plt_play_video_latency"
    }
}