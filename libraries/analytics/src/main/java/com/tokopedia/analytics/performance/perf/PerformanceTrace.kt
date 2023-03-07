package com.tokopedia.analytics.performance.perf

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnDrawListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.ViewTreeObserver.OnPreDrawListener
import android.view.ViewTreeObserver.OnScrollChangedListener
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicReference

@FlowPreview
class PerformanceTrace(val traceName: String) {
    companion object {
        const val TYPE_TTFL = "TTFL"
        const val TYPE_TTIL = "TTIL"
        const val SCROLL_STATE_CHANGED_THRESHOLD = 3
        const val GLOBAL_LAYOUT_DEBOUNCE = 1000L
    }
    private var startCurrentTimeMillis = 0L
    var summaryModel: AtomicReference<SummaryModel> = AtomicReference(SummaryModel())

    val sharedFlow = MutableSharedFlow<View>(1, 0, BufferOverflow.SUSPEND)
    var outputSharedFlow: SharedFlow<Unit> = MutableSharedFlow<Unit>(1, 0, BufferOverflow.SUSPEND)

    val scrollChangedSharedFlow = MutableSharedFlow<View>(1, 0, BufferOverflow.SUSPEND)
    var scrollChangedOutputSharedFlow: SharedFlow<Unit> = MutableSharedFlow<Unit>(1, 0, BufferOverflow.SUSPEND)

    init {
        startCurrentTimeMillis = System.currentTimeMillis()
    }

    fun init(
        v: View,
        scope: LifecycleCoroutineScope,
        onLaunchTimeFinished: (summaryModel: SummaryModel, type: String, view: View) -> Unit

        ) {
        scope.launch(Dispatchers.IO) {
            PerformanceTraceDebugger.logTrace(
                "Initialize performance trace for: $traceName"
            )
            val viewgroup = v as ViewGroup
            setTTFLInflateTrace(viewgroup, onLaunchTimeFinished)
            setTTILInflateTrace(v, viewgroup, onLaunchTimeFinished, scope)
            setupScrollChangeListener(v, scope)
        }
    }

    private fun setupScrollChangeListener(
        v: ViewGroup,
        scope: LifecycleCoroutineScope
    ) {
        val onScrollChangeListener = onScrollChangeView(v)
        var scrollChangedCount = 0
        scrollChangedOutputSharedFlow = scrollChangedSharedFlow.map {
            if (scrollChangedCount >= SCROLL_STATE_CHANGED_THRESHOLD) {
                scope.cancel()
                if (summaryModel.get().timeToInitialLayout != null) {
                    PerformanceTraceDebugger.logTrace(
                        "Performance trace finished."
                    )
                } else {
                    PerformanceTraceDebugger.logTrace(
                        "Performance trace cancelled due to scroll event"
                    )
                }
                v.viewTreeObserver.removeOnScrollChangedListener(onScrollChangeListener)
            }
            scrollChangedCount += 1
        }.shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)
        scrollChangedOutputSharedFlow.launchIn(scope)

        v.viewTreeObserver.addOnScrollChangedListener(onScrollChangeListener)
    }

    private fun setTTILInflateTrace(
        v: View,
        viewgroup: ViewGroup,
        onLaunchTimeFinished: (summaryModel: SummaryModel, type: String, view: View) -> Unit,
        scope: LifecycleCoroutineScope
    ) {
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

    private fun onScrollChangeView(v: View) = OnScrollChangedListener {
        scrollChangedSharedFlow.tryEmit(v)
    }


    private fun setTTFLInflateTrace(
        viewgroup: ViewGroup,
        onLaunchTimeFinished: (summaryModel: SummaryModel, type: String, view: View) -> Unit
    ) {
        PerformanceTraceDebugger.logTrace(
            "Measuring TTFL: $traceName"
        )
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





