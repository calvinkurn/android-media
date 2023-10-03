package com.tokopedia.analytics.performance.fpi

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.FrameMetrics
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import kotlin.math.pow

/**
 * Created by yovi.putra on 18/07/23"
 * Project name: android-tokopedia-core
 **/

class FrameMetricsMonitoring @JvmOverloads constructor(
    private val applicationContext: Context,
    private val forceEnable: Boolean = false
) : Application.ActivityLifecycleCallbacks {

    companion object {
        // ref to DeveloperOptionActivity.PREF_KEY_FPI_MONITORING_POPUP
        private const val PREF_KEY = "fpi_monitoring_popup"

        private const val JANKY_LEVEL_MS = 17f
    }

    private val frameMetricAvailableListener by lazyThreadSafetyNone {
        mutableMapOf<String, Window.OnFrameMetricsAvailableListener?>()
    }

    private val fpiData by lazyThreadSafetyNone {
        FpiPerformanceData()
    }

    private val handler by lazyThreadSafetyNone {
        Handler(Looper.getMainLooper())
    }

    private val frameMetricsPopupWindow by lazyThreadSafetyNone {
        FrameMetricsPopupWindow(applicationContext)
    }

    init {
        applicationContext.setForceConfig()
    }

    override fun onActivityResumed(activity: Activity) {
        if (isActive()) {
            start(activity)
            frameMetricsPopupWindow.show(activity) {
                reset()
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (isActive() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val pageName = activity.getPageName()
            frameMetricAvailableListener[pageName]?.let {
                activity.window.removeOnFrameMetricsAvailableListener(it)
            }
            frameMetricAvailableListener.remove(pageName)
            reset()
        }
    }

    private fun start(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val pageName = activity.getPageName()
            val listener = Window.OnFrameMetricsAvailableListener { _, metrics, _ ->
                val frameMetricsCopy = FrameMetrics(metrics)
                recordFrames(metrics = frameMetricsCopy)
            }

            // register
            activity.window.addOnFrameMetricsAvailableListener(listener, handler)
            frameMetricAvailableListener[pageName] = listener
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun recordFrames(metrics: FrameMetrics) {
        fpiData.incrementAllFrames()

        val totalDurationMs = metrics.getMetric(FrameMetrics.TOTAL_DURATION).toMillis()
        fpiData.incrementDuration(totalDurationMs.orZero())

        if (totalDurationMs > JANKY_LEVEL_MS) {
            fpiData.incrementJankyFrames()
        }

        frameMetricsPopupWindow.updateInfo(fpiData = fpiData)
    }

    private fun reset() {
        fpiData.reset()
        frameMetricsPopupWindow.updateInfo(fpiData)
    }

    private fun isActive(): Boolean =
        GlobalConfig.isAllowDebuggingTools() && applicationContext.isFpiMonitoringEnable()

    private fun Context.isFpiMonitoringEnable(): Boolean = getSharedPreferences(
        PREF_KEY, MODE_PRIVATE
    ).getBoolean(PREF_KEY, forceEnable)

    private fun Context.setForceConfig() {
        val current = getSharedPreferences(PREF_KEY, MODE_PRIVATE).getBoolean(PREF_KEY, true)
        val forceEnableToConfig = current && forceEnable

        // if current is true and force enable is true, force config to true, otherwise no-ops
        if (forceEnableToConfig) {
            getSharedPreferences(PREF_KEY, MODE_PRIVATE).edit {
                putBoolean(PREF_KEY, true)
            }
        }
    }

    private fun Long.toMillis() = div(10.0.pow(6.0))

    private fun Activity.getPageName() = javaClass.simpleName

    // region unused lifecycle
    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
    // endregion
}
