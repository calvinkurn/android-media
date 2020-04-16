package com.tokopedia.analytics.performance.fpi

import android.annotation.TargetApi
import android.os.Build
import android.os.Handler
import android.view.FrameMetrics
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.cachemanager.PersistentCacheManager

class FragmentFramePerformanceIndexMonitoring : LifecycleObserver {
    private val DEFAULT_WARNING_LEVEL_MS = 17f
    private val METRICS_ALL_FRAMES = "all_frames"
    private val METRICS_JANKY_FRAMES = "janky_frames"
    private val METRICS_JANKY_FRAMES_PERCENTAGE = "janky_frames_percentage"
    private val METRICS_FRAME_PERFORMANCE_INDEX = "frame_performance_index"

    private val METRICS_PERFORMANCE_MONITORING = "fpi_%s"

    private var onFrameMetricAvailableListener: Window.OnFrameMetricsAvailableListener? = null
    private var onFrameListener: OnFrameListener? = null
    private var fragment: Fragment? = null
    private var cacheManager: CacheManager? = null
    private var pageName: String? = null

    var mainPerformanceData = FpiPerformanceData()
    private set

    interface OnFrameListener {
        fun onFrameRendered(fpiPerformanceData: FpiPerformanceData)
    }

    private fun getFpiDatabaseModel() = FpiDatabaseModel(fragment.hashCode().toString(), mainPerformanceData)

    fun init(pageName: String,
             fragment: Fragment,
             onFrameListener: OnFrameListener? = null) {
        this.fragment = fragment
        this.pageName = pageName
        this.onFrameListener = onFrameListener
        fragment.activity?.applicationContext?.let {
            this.cacheManager = PersistentCacheManager(it)
        }

        startFrameMetrics()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun sendRemainingPerformanceData() {
        pageName?.let { pageName->
            val cacheFpiDatabaseModel = cacheManager?.get<FpiDatabaseModel>(
                    pageName, FpiDatabaseModel::class.java, null
            )
            cacheFpiDatabaseModel?.let {
                if (it.fragmentHashCode != fragment?.hashCode().toString()) {
                    sendPerformanceMonitoringData(pageName)
                    cacheManager?.delete(pageName)
                } else {
                    mainPerformanceData = it.fpiPerformanceData
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun saveToDatabase() {
        pageName?.let {pageName->
            cacheManager?.put(
                    customId = pageName,
                    objectToPut = getFpiDatabaseModel()
            )
        }
    }

    private fun sendPerformanceMonitoringData(pageName: String) {
        val performanceMonitoring = PerformanceMonitoring()
        performanceMonitoring.startTrace(String.format(METRICS_PERFORMANCE_MONITORING, pageName))
        performanceMonitoring.putMetric(METRICS_ALL_FRAMES, mainPerformanceData.allFrames.toLong())
        performanceMonitoring.putMetric(METRICS_JANKY_FRAMES, mainPerformanceData.jankyFrames.toLong())
        performanceMonitoring.putMetric(METRICS_JANKY_FRAMES_PERCENTAGE, mainPerformanceData.jankyFramePercentage.toLong())
        performanceMonitoring.putMetric(METRICS_FRAME_PERFORMANCE_INDEX, mainPerformanceData.framePerformanceIndex.toLong())
        performanceMonitoring.stopTrace()
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun startFrameMetrics() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && onFrameMetricAvailableListener == null) {
            onFrameMetricAvailableListener = Window.OnFrameMetricsAvailableListener { window, frameMetrics, dropCountSinceLastInvocation ->
                val frameMetricsCopy = FrameMetrics(frameMetrics)
                mainPerformanceData.incrementAllFrames()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val totalDurationMs = (0.000001 * frameMetricsCopy.getMetric(FrameMetrics.TOTAL_DURATION)).toFloat()
                    if (totalDurationMs > DEFAULT_WARNING_LEVEL_MS) {
                        mainPerformanceData.incremenetJankyFrames()
                    }
                    onFrameListener?.onFrameRendered(mainPerformanceData)
                }
            }
            onFrameMetricAvailableListener?.let {
                fragment?.activity?.window?.addOnFrameMetricsAvailableListener(it, Handler())
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun stopFrameMetrics() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && onFrameMetricAvailableListener!=null) {
            onFrameMetricAvailableListener?.let {
                fragment?.activity?.window?.removeOnFrameMetricsAvailableListener(it)
            }
            onFrameMetricAvailableListener = null
        }
    }
}