package com.tokopedia.analytics.performance.perf

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.lifecycle.LifecycleCoroutineScope
import com.tokopedia.abstraction.base.view.listener.TouchListenerActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicReference

@FlowPreview
class PerformanceTrace(val traceName: String) {
    companion object {
        const val TYPE_TTFL = "TTFL"
        const val TYPE_TTIL = "TTIL"
        const val GLOBAL_LAYOUT_DEBOUNCE = 1000L
    }
    private var startCurrentTimeMillis = 0L
    var summaryModel: AtomicReference<SummaryModel> = AtomicReference(SummaryModel())

    val sharedFlow = MutableSharedFlow<View>(1, 0, BufferOverflow.SUSPEND)
    var outputSharedFlow: SharedFlow<Unit> = MutableSharedFlow<Unit>(1, 0, BufferOverflow.SUSPEND)

    init {
        startCurrentTimeMillis = System.currentTimeMillis()
    }

    var performanceTraceJob: Job? = null
    fun init(
        v: View,
        scope: LifecycleCoroutineScope,
        touchListenerActivity: TouchListenerActivity?,
        onLaunchTimeFinished: (summaryModel: SummaryModel, type: String, view: View) -> Unit,
        ) {

        performanceTraceJob = scope.launch(Dispatchers.IO) {
            PerformanceTraceDebugger.logTrace(
                "Initialize performance trace for: $traceName"
            )
            val viewgroup = v as ViewGroup
            setTTFLInflateTrace(viewgroup, onLaunchTimeFinished)
            setTTILInflateTrace(v, viewgroup, onLaunchTimeFinished, scope)
            touchListenerActivity?.addListener { cancelPerformancetrace() }
        }
    }

    fun cancelPerformancetrace() {
        performanceTraceJob?.cancel()
        if (summaryModel.get().timeToInitialLayout != null) {
            PerformanceTraceDebugger.logTrace(
                "Performance trace finished."
            )
        } else {
            PerformanceTraceDebugger.logTrace(
                "Performance trace cancelled due to scroll event"
            )
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
            onLaunchTimeFinished.invoke(summaryModel.get(), TYPE_TTIL, it)
            viewgroup.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutTTIL)
            PerformanceTraceDebugger.logTrace(
                "TTIL Captured: ${summaryModel.get().timeToInitialLayout?.inflateTime} ms"
            )
        }.shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)
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
                    val perfModel = createViewPerformanceModel(viewgroup)
                    validateTTFL(perfModel)
                    PerformanceTraceDebugger.logTrace(
                        "TTFL Captured: ${perfModel.inflateTime} ms"
                    )
                    onLaunchTimeFinished.invoke(summaryModel.get(), TYPE_TTFL, viewgroup)
                    viewgroup.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        )
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





