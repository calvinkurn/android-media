package com.tokopedia.analytics.performance.util

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Handler
import android.util.Log
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
open class JankyFrameMonitoringUtil {

    private var activity: Activity? = null
    private var performanceMonitoring: PerformanceMonitoring? = null
    private var isPerformanceMonitoringActive: Boolean = false

    private var onFrameMetricAvailableListener: Window.OnFrameMetricsAvailableListener? = null
    private val mainPerformanceData = PerformanceData()
    private val DEFAULT_WARNING_LEVEL_MS = 17f

    private val TYPE_INIT = "init"
    private val TYPE_SCROLL = "scroll"
    private val PERF_JANKY_FRAMES_TAG = "janky_frames_%s_%s"
    private val PERF_JANKY_FRAMES_SUB_PAGE_TAG = "janky_frames_%s_%s_%s"

    private val initPerformanceMonitoring = mutableMapOf<String, PerformanceMonitoring>()
    private val initPerformanceDatas = mutableMapOf<String, PerformanceData>()

    private var onFrameListener: OnFrameListener? = null

    interface OnFrameListener {
        fun onFrameRendered(performanceData: PerformanceData)
    }

    fun init(activity: Activity, onFrameListener: OnFrameListener? = null) {
        this.activity = activity
        this.onFrameListener = onFrameListener
        startFrameMetrics()
    }

    open fun recordRecyclerViewScrollPerformance(recyclerView: RecyclerView,
                                            pageName: String,
                                            subPageName: String = "") {
        val tag = getPageTag(pageName, subPageName)

        val performanceMonitoring = PerformanceMonitoring()
        performanceMonitoring.startTrace(tag)

        var startAllFramesCount = 0
        var endAllFramesCount = 0

        var startJankyFramesCount = 0
        var endJankyFramesCount = 0

        var isScrolling = false

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when(newState) {
                    SCROLL_STATE_DRAGGING -> {
                        if (!isScrolling) {
                            startAllFramesCount = mainPerformanceData.allFrames
                            startJankyFramesCount = mainPerformanceData.jankyFrames
                        }
                        isScrolling = true
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

                        onFrameListener?.onFrameRendered(performanceData)
                        performanceMonitoring.stopTrace()
                        isScrolling = false

                        startAllFramesCount = mainPerformanceData.allFrames
                        startJankyFramesCount = mainPerformanceData.jankyFrames
                    }
                }
            }
        })
    }

    open fun getPageTag(pageName: String, subPageName: String): String {
        return if (subPageName.isEmpty()) String.format(PERF_JANKY_FRAMES_TAG, TYPE_SCROLL, pageName)
        else String.format(PERF_JANKY_FRAMES_SUB_PAGE_TAG, TYPE_SCROLL, pageName, subPageName)
    }

    fun startInitPerformanceMonitoring(pageName: String) {
        val tag = String.format(PERF_JANKY_FRAMES_TAG, TYPE_INIT, pageName)
        val performanceData = PerformanceData(
                mainPerformanceData.allFrames, mainPerformanceData.jankyFrames
        )
        val performanceMonitoring = PerformanceMonitoring()
        performanceMonitoring.startTrace(tag)

        initPerformanceDatas.put(tag, performanceData)
        initPerformanceMonitoring.put(tag, performanceMonitoring)
    }

    fun stopInitPerformanceMonitoring(pageName: String) {
        val tag = String.format(PERF_JANKY_FRAMES_TAG, TYPE_INIT, pageName)

        val initPerformanceData = initPerformanceDatas[tag]
        initPerformanceData?.let {
            val endAllFramesCount = mainPerformanceData.allFrames
            val endJankyFramesCount = mainPerformanceData.jankyFrames

            val allFramesInSession = endAllFramesCount - it.allFrames
            val allJankyFramesInSession = endJankyFramesCount - it.jankyFrames
            val performanceData = PerformanceData(
                    allFrames = allFramesInSession,
                    jankyFrames = allJankyFramesInSession
            )
            val performanceMonitoring = initPerformanceMonitoring[tag]
            performanceMonitoring?.let {
                it.putMetric(performanceData.allFramesTag, performanceData.allFrames.toLong())
                it.putMetric(performanceData.jankyFramesTag, performanceData.jankyFrames.toLong())
                it.putMetric(performanceData.jankyFramesPercentageTag, performanceData.jankyFramePercentage.toLong())

                it.stopTrace()
                initPerformanceMonitoring.remove(tag)
                initPerformanceDatas.remove(tag)
            }
        }
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
        performanceMonitoring?.stopTrace()
    }

}