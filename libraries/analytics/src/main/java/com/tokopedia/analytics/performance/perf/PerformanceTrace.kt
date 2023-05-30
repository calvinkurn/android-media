package com.tokopedia.analytics.performance.perf

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.lifecycle.LifecycleCoroutineScope
import com.tokopedia.abstraction.base.view.listener.TouchListenerActivity
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.analytics.performance.perf.PerformanceTraceDebugger.takeScreenshot
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_PERFORMANCE_TRACE
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicReference

@FlowPreview
class PerformanceTrace(val traceName: String) {
    companion object {
        private const val PERF_TIMEOUT = 10000
        private const val FLOW_TIMEOUT = 5000L
        private const val FLOW_REPLAY = 1
        const val TYPE_TTFL = "TTFL"
        const val TYPE_TTIL = "TTIL"
        const val GLOBAL_LAYOUT_DEBOUNCE = 500L
        const val ATTR_CONDITION = "State"
        const val STATE_ERROR = "page_error"
        const val STATE_PARTIALLY_ERROR = "page_partially_error"
        const val STATE_TOUCH = "user_touch"
        const val STATE_SUCCESS = "success"
        const val STATE_TIMEOUT = "timeout"
    }
    private var startCurrentTimeMillis = 0L
    private var currentLoadableComponentList = mutableListOf<LoadableComponent>()

    var summaryModel: AtomicReference<SummaryModel> = AtomicReference(SummaryModel())
    var outputSharedFlow: SharedFlow<Unit> = MutableSharedFlow<Unit>(1, 0, BufferOverflow.SUSPEND)

    val sharedFlow = MutableSharedFlow<View>(1, 0, BufferOverflow.SUSPEND)
    val performanceMonitoring = PerformanceMonitoring()

    init {
        startCurrentTimeMillis = System.currentTimeMillis()
        performanceMonitoring.startTrace("perf_trace_$traceName")
    }

    var performanceTraceJob: Job? = null

    fun debugPerformanceTrace(activity: Activity?, summaryModel: SummaryModel, type: String, view: View) {
        activity?.takeScreenshot(type, view)
        if (type == TYPE_TTIL) {
            Toaster.build(view, "" +
                "TTFL: ${summaryModel.timeToFirstLayout?.inflateTime} ms \n" +
                "TTIL: ${summaryModel.timeToInitialLayout?.inflateTime} ms \n" ).show()

        }
    }

    fun setLoadableComponentList(objList: List<Any>) {
        currentLoadableComponentList = objList.filterIsInstance<LoadableComponent>().toMutableList()
    }

    fun isLoadingFinished(): Boolean {
        if (timeoutExceeded()) return true
        return currentLoadableComponentList.any { it is LoadableComponent && !it.isLoading() }
    }

    private fun timeoutExceeded(): Boolean {
        val isTimeout = System.currentTimeMillis() - startCurrentTimeMillis >= PERF_TIMEOUT
        if (isTimeout) {
            setPageState(STATE_TIMEOUT)
        }
        return isTimeout
    }

    fun init(
        v: View,
        scope: LifecycleCoroutineScope,
        touchListenerActivity: TouchListenerActivity?,
        onLaunchTimeFinished: (summaryModel: SummaryModel, type: String, view: View) -> Unit =
            { _,_,_ -> },
        ) {
        val remoteConfig = FirebaseRemoteConfigImpl(v.context)
        val isPerformanceTraceEnabled = remoteConfig.getBoolean(
            ENABLE_PERFORMANCE_TRACE, true
        )
        if (isPerformanceTraceEnabled) {
            performanceTraceJob = scope.launchCatchError(
                Dispatchers.IO, block = {
                    PerformanceTraceDebugger.logTrace(
                        "Initialize performance trace for: $traceName"
                    )
                    val viewgroup = v as ViewGroup
                    setTTFLInflateTrace(viewgroup, onLaunchTimeFinished)
                    setTTILInflateTrace(v, viewgroup, onLaunchTimeFinished, scope)
                    touchListenerActivity?.addListener { cancelPerformanceTrace(STATE_TOUCH) }
                }) {
            }
        }
    }

    fun setPageState(state: String) {
        performanceMonitoring.putCustomAttribute(
            ATTR_CONDITION, state)
    }

    fun cancelPerformanceTrace(state: String) {
        if (performanceTraceJob?.isCancelled == false) {
            performanceTraceJob?.cancel()
            performanceMonitoring.putCustomAttribute(
                ATTR_CONDITION, state)
            performanceMonitoring.stopTrace()

            if (summaryModel.get().timeToInitialLayout != null) {
                PerformanceTraceDebugger.logTrace(
                    "Performance TTFL trace finished."
                )
            } else {
                PerformanceTraceDebugger.logTrace(
                    "Performance TTIL trace cancelled due to scroll event / error"
                )
            }
        }
    }

    private suspend fun setTTILInflateTrace(
        v: View,
        viewgroup: ViewGroup,
        onLaunchTimeFinished: (summaryModel: SummaryModel, type: String, view: View) -> Unit,
        scope: LifecycleCoroutineScope
    ) {
        yield()
        val onGlobalLayoutTTIL = onGlobalLayoutTTIL(v)
        viewgroup.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutTTIL)

        outputSharedFlow = sharedFlow.debounce(GLOBAL_LAYOUT_DEBOUNCE).map {
            if (isLoadingFinished()) {
                onTTILFinished(onLaunchTimeFinished, it, viewgroup, onGlobalLayoutTTIL)
            }
        }.shareIn(scope, SharingStarted.WhileSubscribed(FLOW_TIMEOUT), FLOW_REPLAY)
        outputSharedFlow.launchIn(scope)
    }

    private fun onGlobalLayoutTTIL(v: View) = OnGlobalLayoutListener {
        sharedFlow.tryEmit(v)
        val perfModel = createViewPerformanceModel(v)
        validateTTIL(perfModel)
    }

    private suspend fun setTTFLInflateTrace(
        viewgroup: ViewGroup,
        onLaunchTimeFinished: (summaryModel: SummaryModel, type: String, view: View) -> Unit
    ) {
        PerformanceTraceDebugger.logTrace(
            "Measuring TTFL: $traceName"
        )
        yield()
        viewgroup.viewTreeObserver.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    onTTFLFinished(viewgroup, onLaunchTimeFinished)
                }
            }
        )
    }

    private fun OnGlobalLayoutListener.onTTFLFinished(
        viewgroup: ViewGroup,
        onLaunchTimeFinished: (summaryModel: SummaryModel, type: String, view: View) -> Unit
    ) {
        val perfModel = createViewPerformanceModel(viewgroup)
        validateTTFL(perfModel)
        PerformanceTraceDebugger.logTrace(
            "TTFL Captured: ${perfModel.inflateTime} ms"
        )
        onLaunchTimeFinished.invoke(summaryModel.get(), TYPE_TTFL, viewgroup)
        viewgroup.viewTreeObserver.removeOnGlobalLayoutListener(this)
        performanceMonitoring.putMetric(TYPE_TTFL, perfModel.inflateTime)
    }

    private fun onTTILFinished(
        onLaunchTimeFinished: (summaryModel: SummaryModel, type: String, view: View) -> Unit,
        it: View,
        viewgroup: ViewGroup,
        onGlobalLayoutTTIL: OnGlobalLayoutListener
    ) {
        PerformanceTraceDebugger.logTrace(
            "TTIL Captured: ${summaryModel.get().timeToInitialLayout?.inflateTime} ms"
        )
        onLaunchTimeFinished.invoke(summaryModel.get(), TYPE_TTIL, it)
        viewgroup.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutTTIL)
        performanceMonitoring.putMetric(TYPE_TTIL, summaryModel.get().timeToInitialLayout?.inflateTime?:0L)
        cancelPerformanceTrace(STATE_SUCCESS)
    }

    private fun createViewPerformanceModel(view: View) = ViewPerformanceModel(
        id = view.id,
        idName = getId(view),
        width = view.width,
        height = view.height,
        name = view.javaClass.simpleName,
        inflateTime = System.currentTimeMillis() - startCurrentTimeMillis,
        isViewGroup = isViewGroup(view)
    )

    private fun isViewGroup(v: View): Boolean {
        return v is ViewGroup
    }

    private fun getId(view: View): String {
        return if (view.id == View.NO_ID) "no-id-"+view.hashCode().toString() else view.resources.getResourceName(view.id)
    }

    private fun validateTTFL(viewPerfFromNode: ViewPerformanceModel?) {
        summaryModel.get()?.let { it ->
            summaryModel.set(
                it.copy(
                    timeToFirstLayout = viewPerfFromNode
                )
            )
        }
    }

    private fun validateTTIL(viewPerfFromNode: ViewPerformanceModel?) {
        summaryModel.get()?.let {
            summaryModel.set(
                it.copy(
                    timeToInitialLayout = viewPerfFromNode
                )
            )
        }
    }
}





