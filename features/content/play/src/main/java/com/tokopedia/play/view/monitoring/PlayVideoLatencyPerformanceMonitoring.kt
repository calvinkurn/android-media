package com.tokopedia.play.view.monitoring

import android.util.Log
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.play_common.util.PlayConnectionCommon
import com.tokopedia.play_common.util.PlayLiveRoomMetricsCommon
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
        Log.d("SUKSES", mTotalDuration.toString())
        mPerformanceMonitoring = PerformanceMonitoring.start(PLAY_VIDEO_LATENCY_TRACE)
        mHasStarted = true
    }

    fun stop() {
        if (!mHasStarted) throw Throwable("Please call start() first")

        mPerformanceMonitoring?.stopTrace()
        mTotalDuration = System.currentTimeMillis() - mTotalDuration
        mHasStarted = false

        PlayLiveRoomMetricsCommon.getTimeToFirstByte(mTotalDuration)
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