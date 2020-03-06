package com.tokopedia.analytics.performance.util

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Handler
import android.view.FrameMetrics
import android.view.Window
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.tokopedia.analytics.performance.PerformanceMonitoring
import java.lang.StringBuilder

/**
 * @author by yoasfs on 2020-03-03
 */
class JankyFrameMonitoringUtil {

    private var activity: Activity? = null
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private var isPerformanceMonitoringActive: Boolean = false

    private var onFrameMetricAvailableListener: Window.OnFrameMetricsAvailableListener? = null
    private val mainPerformanceData = PerformanceData()
    private val DEFAULT_WARNING_LEVEL_MS = 17f

    private val TYPE_INIT = "init"
    private val TYPE_SCROLL = "scroll"
    private val PERF_JANKY_FRAMES_TAG = "janky_frames_%s_%s"
    private val PERF_JANKY_FRAMES_SUB_PAGE_TAG = "janky_frames_%s_%s_%s"

    fun init(activity: Activity) {
        this.activity = activity
        startFrameMetrics()
    }

    fun recordRecyclerViewScrollPerformance(recyclerView: RecyclerView,
                                            pageName: String,
                                            subPageName: String = "") {
        var tag = if (subPageName.isNotEmpty()) String.format(PERF_JANKY_FRAMES_TAG, TYPE_SCROLL, pageName)
        else String.format(PERF_JANKY_FRAMES_SUB_PAGE_TAG, TYPE_SCROLL, pageName, subPageName)

        val performanceMonitoring = PerformanceMonitoring()
        performanceMonitoring.startTrace(tag)

        var startAllFramesCount = 0
        var endAllFramesCount = 0

        var startJankyFramesCount = 0
        var endJankyFramesCount = 0

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when(newState) {
                    SCROLL_STATE_DRAGGING -> {
                        startAllFramesCount = mainPerformanceData.allFrames
                        startJankyFramesCount = mainPerformanceData.jankyFrames
                    }
                    SCROLL_STATE_IDLE -> {
                        endAllFramesCount = mainPerformanceData.allFrames
                        endJankyFramesCount = mainPerformanceData.jankyFrames

                        val allFramesInSession = endAllFramesCount - startAllFramesCount
                        val allJankyFramesInSession = endJankyFramesCount - startJankyFramesCount
                        val performanceData = PerformanceData(
                                allFrames = allFramesInSession,
                                jankyFrames = allJankyFramesInSession
                        )

                        performanceMonitoring.putMetric(performanceData.allFramesTag, performanceData.allFrames.toLong())
                        performanceMonitoring.putMetric(performanceData.jankyFramesTag, performanceData.jankyFrames.toLong())
                        performanceMonitoring.putMetric(performanceData.jankyFramesPercentageTag, performanceData.jankyFramePercentage.toLong())

                        performanceMonitoring.stopTrace()
                    }
                }
            }
        })
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun startFrameMetrics() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !isPerformanceMonitoringActive) {
            isPerformanceMonitoringActive = true
            onFrameMetricAvailableListener = Window.OnFrameMetricsAvailableListener { window, frameMetrics, dropCountSinceLastInvocation ->
                val frameMetricsCopy = FrameMetrics(frameMetrics)
                mainPerformanceData.incrementAllFrames()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val totalDurationMs = (0.000001 * frameMetricsCopy.getMetric(FrameMetrics.TOTAL_DURATION)).toFloat()
                    if (totalDurationMs > DEFAULT_WARNING_LEVEL_MS) {
                        mainPerformanceData.incremenetJankyFrames()
                    }
                }
            }
            onFrameMetricAvailableListener?.let {
                activity?.window?.addOnFrameMetricsAvailableListener(it, Handler())
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun stopFrameMetrics() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isPerformanceMonitoringActive) {
            isPerformanceMonitoringActive= false
            onFrameMetricAvailableListener?.let {
                activity?.window?.removeOnFrameMetricsAvailableListener(it)
            }
            onFrameMetricAvailableListener = null
        }
        performanceMonitoring.stopTrace()
    }

}