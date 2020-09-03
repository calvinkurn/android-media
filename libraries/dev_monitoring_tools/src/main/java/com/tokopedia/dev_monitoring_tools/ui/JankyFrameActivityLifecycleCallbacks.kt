package com.tokopedia.dev_monitoring_tools.ui

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.FrameMetrics
import android.view.Window
import timber.log.Timber
import java.util.*

/**
 * Created by Vishal Gupta on 17/10/2018
 */
@SuppressLint("NewApi")
class JankyFrameActivityLifecycleCallbacks private constructor() : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {
        startFrameMetrics(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        stopFrameMetrics(activity)
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}
    override fun onActivityDestroyed(activity: Activity) {}
    private var warningLevelMs = 0f
    private var errorLevelMs = 0f
    private var showWarning = false
    private var showError = false
    private val frameMetricsAvailableListenerMap: MutableMap<String, Window.OnFrameMetricsAvailableListener> = HashMap()

    @TargetApi(Build.VERSION_CODES.N)
    fun startFrameMetrics(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val activityName = activity.javaClass.simpleName
            val listener: Window.OnFrameMetricsAvailableListener = object : Window.OnFrameMetricsAvailableListener {
                private var allFrames = 0
                private var jankyFrames = 0
                override fun onFrameMetricsAvailable(window: Window, frameMetrics: FrameMetrics, dropCountSinceLastInvocation: Int) {
                    val frameMetricsCopy = FrameMetrics(frameMetrics)
                    allFrames++
                    val totalDurationMs = (0.000001 * frameMetricsCopy.getMetric(FrameMetrics.TOTAL_DURATION)).toFloat()
                    if (totalDurationMs > warningLevelMs) {
                        jankyFrames++
                        var msg = String.format("Janky frame detected on %s with total duration: %.2fms\n", activityName, totalDurationMs)
                        val layoutMeasureDurationMs = (0.000001 * frameMetricsCopy.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION)).toFloat()
                        val drawDurationMs = (0.000001 * frameMetricsCopy.getMetric(FrameMetrics.DRAW_DURATION)).toFloat()
                        val gpuCommandMs = (0.000001 * frameMetricsCopy.getMetric(FrameMetrics.COMMAND_ISSUE_DURATION)).toFloat()
                        val othersMs = totalDurationMs - layoutMeasureDurationMs - drawDurationMs - gpuCommandMs
                        val jankyPercent = jankyFrames.toFloat() / allFrames * 100
                        msg += String.format("Layout/measure: %.2fms, draw:%.2fms, gpuCommand:%.2fms others:%.2fms\n",
                                layoutMeasureDurationMs, drawDurationMs, gpuCommandMs, othersMs)
                        msg += "Janky frames: $jankyFrames/$allFrames($jankyPercent%)"
                        if (showWarning && totalDurationMs > errorLevelMs) {
                            Timber.d(msg)
                        } else if (showError) {
                            Timber.d(msg)
                        }
                    }
                }
            }
            activity.window.addOnFrameMetricsAvailableListener(listener, Handler())
            frameMetricsAvailableListenerMap[activityName] = listener
        } else {
            Timber.d("FrameMetrics can work only with Android SDK 24 (Nougat) and higher")
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun stopFrameMetrics(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val activityName = activity.javaClass.name
            val onFrameMetricsAvailableListener = frameMetricsAvailableListenerMap[activityName]
            if (onFrameMetricsAvailableListener != null) {
                activity.window.removeOnFrameMetricsAvailableListener(onFrameMetricsAvailableListener)
                frameMetricsAvailableListenerMap.remove(activityName)
            }
        }
    }

    class Builder {
        private var warningLevelMs = DEFAULT_WARNING_LEVEL_MS
        private var errorLevelMs = DEFAULT_ERROR_LEVEL_MS
        private var showWarnings = true
        private var showErrors = true
        fun warningLevelMs(warningLevelMs: Float): Builder {
            this.warningLevelMs = warningLevelMs
            return this
        }

        fun errorLevelMs(errorLevelMs: Float): Builder {
            this.errorLevelMs = errorLevelMs
            return this
        }

        fun showWarnings(show: Boolean): Builder {
            showWarnings = show
            return this
        }

        fun showErrors(show: Boolean): Builder {
            showErrors = show
            return this
        }

        fun build(): JankyFrameActivityLifecycleCallbacks {
            val jankyFrameActivityLifecycleCallbacks = JankyFrameActivityLifecycleCallbacks()
            jankyFrameActivityLifecycleCallbacks.warningLevelMs = warningLevelMs
            jankyFrameActivityLifecycleCallbacks.errorLevelMs = errorLevelMs
            jankyFrameActivityLifecycleCallbacks.showError = showErrors
            jankyFrameActivityLifecycleCallbacks.showWarning = showWarnings
            return jankyFrameActivityLifecycleCallbacks
        }
    }

    companion object {
        private const val DEFAULT_WARNING_LEVEL_MS = 17f
        private const val DEFAULT_ERROR_LEVEL_MS = 34f
    }
}