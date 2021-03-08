package com.tokopedia.play.view.monitoring

import com.tokopedia.analytics.performance.PerformanceMonitoring
import javax.inject.Inject


/**
 * Created by mzennis on 08/03/21.
 */
class PlayVideoLatencyPerformanceMonitoring @Inject constructor() {

    private lateinit var performanceMonitoring: PerformanceMonitoring

    fun start() {
        performanceMonitoring.startTrace(PLAY_VIDEO_LATENCY_TRACE)
    }

    fun stop() {
        performanceMonitoring.stopTrace()
    }

    companion object {
        private const val PLAY_VIDEO_LATENCY_TRACE = "plt_play_video_latency"
    }
}