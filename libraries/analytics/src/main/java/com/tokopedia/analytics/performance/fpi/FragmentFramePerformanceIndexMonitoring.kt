package com.tokopedia.analytics.performance.fpi

import android.annotation.TargetApi
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.FrameMetrics
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.cachemanager.PersistentCacheManager
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext
import kotlin.math.pow


class FragmentFramePerformanceIndexMonitoring : LifecycleEventObserver, CoroutineScope {
    private val DEFAULT_WARNING_LEVEL_MS = 17f
    private val METRICS_ALL_FRAMES = "all_frames"
    private val METRICS_JANKY_FRAMES = "janky_frames"
    private val METRICS_JANKY_FRAMES_PERCENTAGE = "janky_frames_percentage"
    private val METRICS_FRAME_PERFORMANCE_INDEX = "frame_performance_index"

    private val METRICS_PERFORMANCE_MONITORING = "fpi_%s"

    private var onFrameMetricAvailableListener: Window.OnFrameMetricsAvailableListener? = null
    private var onFrameListener: OnFrameListener? = null
    var fragment: Fragment? = null
    private var cacheManager: CacheManager? = null
    private var pageName: String? = null
    private val durationDivider = 10.0.pow(6.0) //

    var mainPerformanceData: FpiPerformanceData? = FpiPerformanceData()
    private set

    protected val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + masterJob

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
        fragment.viewLifecycleOwner.lifecycle.addObserver(this)
    }

    fun onFragmentHidden(isHidden: Boolean) {
        if (isHidden) {
            stopRecordingFramePerformanceIndex()
        } else {
            startRecordingFramePerformanceIndex()
        }
    }

    private fun startRecordingFramePerformanceIndex() {
        launch {
            pageName?.let { pageName ->
                val cacheFpiDatabaseModel = cacheManager?.get<FpiDatabaseModel>(
                        pageName, FpiDatabaseModel::class.java, null
                )
                cacheFpiDatabaseModel?.let {
                    if (it.fragmentHashCode != fragment?.hashCode().toString()) {
                        withContext(Dispatchers.Main) {
                            sendPerformanceMonitoringData(it.fpiPerformanceData, pageName)
                        }
                        cacheManager?.delete(pageName)
                    } else {
                        mainPerformanceData = it.fpiPerformanceData
                    }
                }
            }
            startFrameMetrics()
        }
    }

    private fun stopRecordingFramePerformanceIndex() {
        launch {
            pageName?.let { pageName ->
                cacheManager?.put(
                        customId = pageName,
                        objectToPut = getFpiDatabaseModel()
                )
            }
        }
        stopFrameMetrics()
    }

    private fun sendPerformanceMonitoringData(performanceData: FpiPerformanceData?, pageName: String) {
        val performanceMonitoring = PerformanceMonitoring()
        performanceMonitoring.startTrace(String.format(METRICS_PERFORMANCE_MONITORING, pageName))
        performanceData?.let {
            performanceMonitoring.putMetric(METRICS_ALL_FRAMES, performanceData.allFrames.toLong())
            performanceMonitoring.putMetric(METRICS_JANKY_FRAMES, performanceData.jankyFrames.toLong())
            performanceMonitoring.putMetric(METRICS_JANKY_FRAMES_PERCENTAGE, performanceData.jankyFramePercentage.toLong())
            performanceMonitoring.putMetric(METRICS_FRAME_PERFORMANCE_INDEX, performanceData.framePerformanceIndex.toLong())
        }
        performanceMonitoring.stopTrace()
    }

    @TargetApi(Build.VERSION_CODES.N)
    private suspend fun startFrameMetrics() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && onFrameMetricAvailableListener == null) {
            withContext(Dispatchers.Main) {
                onFrameMetricAvailableListener = Window.OnFrameMetricsAvailableListener { window, frameMetrics, _ ->
                    val frameMetricsCopy = FrameMetrics(frameMetrics)
                    recordFrames(frameMetricsCopy)

                    /*(window.decorView as? ViewGroup)?.let {
                        getAllViews(it)
                    }*/
                }
                onFrameMetricAvailableListener?.let {
                    fragment?.activity?.window?.addOnFrameMetricsAvailableListener(it, Handler(Looper.getMainLooper()))
                }
            }
        }
    }

    private val viewTreeMap = mutableMapOf<String, Set<Long>>()
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getAllViews(
        viewGroup: ViewGroup
    ) {
        for (i in 0 until viewGroup.childCount) {
            val childView = viewGroup.getChildAt(i)
            val globalLayoutProcess =
                GlobalLayoutProcess(viewRef = WeakReference(childView)) { name, time ->
                    val current = viewTreeMap[name].orEmpty().toMutableSet()
                    current.add(time)
                    viewTreeMap[name] = current
                    Timber.tag("GlobalLayoutProcess").d("$name -> $current ms")
                }
            childView.viewTreeObserver.addOnPreDrawListener(globalLayoutProcess)
            childView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutProcess)

            if (childView is ViewGroup) {
                getAllViews(childView)
            }
        }
    }

    inner class GlobalLayoutProcess(
        val viewRef: WeakReference<View>,
        val completed: (viewName: String, time: Long) -> Unit
    ) : ViewTreeObserver.OnGlobalLayoutListener, ViewTreeObserver.OnPreDrawListener {

        private val view by lazy { viewRef.get() }
        private var startTime = 0L
        private var hasEmit: Boolean = false

        override fun onGlobalLayout() {
            if (startTime == 0L || hasEmit) return

            val view = view ?: return
            val context = view.context
            view.viewTreeObserver.removeOnGlobalLayoutListener(this)

            val viewName = try {
                context.resources.getResourceName(view.id)
            } catch (e: Throwable) {
                view.id.toString()
            }

            val total = System.currentTimeMillis() - startTime
            completed(viewName, total)
            hasEmit = true
        }

        override fun onPreDraw(): Boolean {
            startTime = System.currentTimeMillis()
            view?.viewTreeObserver?.removeOnPreDrawListener(this)
            return true
        }
    }

    private fun recordFrames(frameMetricsCopy: FrameMetrics) {
        mainPerformanceData?.incrementAllFrames()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val totalDurationMs = frameMetricsCopy.getMetric(FrameMetrics.TOTAL_DURATION).div(durationDivider)
            if (totalDurationMs > DEFAULT_WARNING_LEVEL_MS) {
                mainPerformanceData?.incremenetJankyFrames()
            }
            mainPerformanceData?.let { onFrameListener?.onFrameRendered(it) }
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

    private fun flush(){
        stopFrameMetrics()
        if (isActive && !masterJob.isCancelled){
            masterJob.children.map { it.cancel() }
        }
    }

    fun onDestroyView() {
        Timber.tag("viewTreeMap").d(viewTreeMap.map {
            "${it.key} -> ${it.value}ms"
        }.joinToString("\n"))
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                startRecordingFramePerformanceIndex()
            }
            Lifecycle.Event.ON_PAUSE -> {
                stopRecordingFramePerformanceIndex()
            }
            Lifecycle.Event.ON_DESTROY -> {
                flush()
            }
            else -> {
                // no-ops
            }
        }
    }
}
