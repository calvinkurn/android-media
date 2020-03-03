package com.tokopedia.feedplus.view.util

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Handler
import android.view.FrameMetrics
import android.view.Window
import java.lang.StringBuilder

/**
 * @author by yoasfs on 2020-03-03
 */
class JankyFrameMonitoringUtil(val activity: Activity, val keyScreen: String) {

    private lateinit var performanceData: PerformanceData
    private var onFrameMetricAvailableListener: Window.OnFrameMetricsAvailableListener? = null
    private val KEYSCREEN_FRAMES_ALL: String = StringBuilder().append(keyScreen).append("_all_frames").toString()
    private val KEYSCREEN_FRAMES_JANKY: String = StringBuilder().append(keyScreen).append("_janky_frames").toString()
    private val KEYSCREEN_FRAMES_PERCENTAGES: String = StringBuilder().append(keyScreen).append("_janky_frames_percentages").toString()
    private val DEFAULT_WARNING_LEVEL_MS = 17f


    @TargetApi(Build.VERSION_CODES.N)
    fun startFrameMetrics() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setupMetrics()
            onFrameMetricAvailableListener = Window.OnFrameMetricsAvailableListener { window, frameMetrics, dropCountSinceLastInvocation ->
                val frameMetricsCopy = FrameMetrics(frameMetrics)
                incrementAllFramesFragmentMetrics(frameMetricsCopy)
            }
            onFrameMetricAvailableListener?.let {
                activity.window.addOnFrameMetricsAvailableListener(it, Handler())
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun stopFrameMetrics(): PerformanceData {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            onFrameMetricAvailableListener?.let {
                activity.window.removeOnFrameMetricsAvailableListener(it)
            }
            onFrameMetricAvailableListener = null
        }
        return performanceData
    }

    private fun setupMetrics() {
        performanceData = PerformanceData(KEYSCREEN_FRAMES_ALL, KEYSCREEN_FRAMES_JANKY, KEYSCREEN_FRAMES_PERCENTAGES)
    }

    private fun incrementAllFramesFragmentMetrics(frameMetricsCopy: FrameMetrics) {
        performanceData.let {
            it.incrementAllFrames()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val totalDurationMs = (0.000001 * frameMetricsCopy.getMetric(FrameMetrics.TOTAL_DURATION)).toFloat()
                if (totalDurationMs > DEFAULT_WARNING_LEVEL_MS) {
                    it.incremenetJankyFrames()
                }
            }
        }
    }


}