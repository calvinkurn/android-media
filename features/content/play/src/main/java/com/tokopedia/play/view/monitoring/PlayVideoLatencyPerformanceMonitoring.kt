package com.tokopedia.play.view.monitoring

import com.tokopedia.analytics.performance.PerformanceMonitoring
import javax.inject.Inject


/**
 * Created by mzennis on 08/03/21.
 */
class PlayVideoLatencyPerformanceMonitoring @Inject constructor() {

    val totalDuration: Long
        get() = mTotalDuration

    val hasStarted: Boolean
        get() = mHasStarted

    private var mPerformanceMonitoring: PerformanceMonitoring? = null
    private var mTotalDuration = 0L
    private var mHasStarted = false

    fun start() {
        reset()

        mTotalDuration = System.currentTimeMillis()
        mPerformanceMonitoring = PerformanceMonitoring.start(PLAY_VIDEO_LATENCY_TRACE)
        mHasStarted = true
    }

    fun stop() {
        if (!mHasStarted) throw Throwable("Please call start() first")

        mPerformanceMonitoring?.stopTrace()
        mTotalDuration = System.currentTimeMillis() - mTotalDuration
        mHasStarted = false
    }

    fun reset() {
        mPerformanceMonitoring = null
        mTotalDuration = 0L
        mHasStarted = false
    }

    companion object {
        private const val PLAY_VIDEO_LATENCY_TRACE = "plt_play_video_latency"
    }
}