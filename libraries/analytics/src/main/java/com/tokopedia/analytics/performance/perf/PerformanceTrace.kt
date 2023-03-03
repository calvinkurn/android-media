package com.tokopedia.analytics.performance.perf

import android.os.*
import android.view.Choreographer
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import java.util.concurrent.atomic.AtomicReference

class PerformanceTrace(val traceName: String) {
    companion object {
        private const val TYPE_TTFL = "TTFL"
        private const val TYPE_TTIL = "TTIL"
    }
    private var startCurrentTimeMillis = 0L
    var summaryModel: AtomicReference<SummaryModel> = AtomicReference(SummaryModel())

    init {
        startCurrentTimeMillis = System.currentTimeMillis()
    }

    fun init(
        v: View,
        targetId: Int? = null,
        onLaunchTimeFinished: (summaryModel: SummaryModel, type: String) -> Unit) {
        PerformanceTraceDebugger.logTrace(
            "Initialize performance trace for: $traceName"
        )
        val viewgroup = v as ViewGroup
        setTTFLInflateTrace(viewgroup, onLaunchTimeFinished)
        setTTILInflateTrace(viewgroup, targetId, onLaunchTimeFinished)
    }

    private fun setTTILInflateTrace(
        viewgroup: ViewGroup,
        targetId: Int?,
        onLaunchTimeFinished: (summaryModel: SummaryModel, type: String) -> Unit
    ) {
        PerformanceTraceDebugger.logTrace(
            "Measuring TTIL: $traceName, TargetView: $targetId"
        )
        viewgroup.viewTreeObserver.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (targetId != null) {
                        viewgroup.findViewById<View>(targetId)?.setOnViewFillingViewport {
                            finishTTILMonitoring(viewgroup, onLaunchTimeFinished)
                        }
                    } else {
                        setTTILInflateTraceWithoutTarget(viewgroup, onLaunchTimeFinished)
                        viewgroup.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            }
        )
    }

    private fun setTTFLInflateTrace(
        viewgroup: ViewGroup,
        onLaunchTimeFinished: (summaryModel: SummaryModel, type: String) -> Unit
    ) {
        PerformanceTraceDebugger.logTrace(
            "Measuring TTFL: $traceName"
        )
        viewgroup.viewTreeObserver.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    doOnNextFrame {
                        val perfModel = createViewPerformanceModel(viewgroup)
                        validateTTFL(perfModel)
                        PerformanceTraceDebugger.logTrace(
                            "TTFL Captured: ${perfModel.inflateTime} ms"
                        )
                        onLaunchTimeFinished.invoke(summaryModel.get(), TYPE_TTFL)
                    }
                    viewgroup.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        )
    }

    private fun setTTILInflateTraceWithoutTarget(
        viewgroup: ViewGroup,
        onLaunchTimeFinished: (summaryModel: SummaryModel, type: String) -> Unit
    ) {
        PerformanceTraceDebugger.logTrace(
            "Measuring TTIL without targetId..."
        )
        viewgroup.viewTreeObserver.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val defaultView = getInnerRootViewGroup(viewgroup)
                    defaultView.setOnViewFillingViewport {
                        finishTTILMonitoring(
                            viewgroup, onLaunchTimeFinished
                        )
                        viewgroup.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        PerformanceTraceDebugger.logTrace("Remove")
                    }
                }
            }
        )
    }

    private fun OnGlobalLayoutListener.finishTTILMonitoring(
        viewgroup: ViewGroup,
        onLaunchTimeFinished: (summaryModel: SummaryModel, type: String) -> Unit
    ) {
        doOnNextFrame {
            val perfModel = createViewPerformanceModel(viewgroup)
            validateTTIL(perfModel)
            PerformanceTraceDebugger.logTrace(
                "TTIL Captured: ${perfModel.inflateTime} ms"
            )
            onLaunchTimeFinished.invoke(summaryModel.get(), TYPE_TTIL)
        }
        viewgroup.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    /**
     * Get the inner ViewGroup with max height
     */
    private fun getInnerRootViewGroup(viewGroup: ViewGroup): ViewGroup {
        for (i in 0 until viewGroup.childCount) {
            val v1 = viewGroup.getChildAt(i)

            if (v1 is ViewGroup && v1.heightIsFillingViewport()) {
                return getInnerRootViewGroup(v1)
            }
        }
        return viewGroup
    }

    /**
     * Use postFrameCallback to improve accuracy of finish trace exactly after
     * next frame rendered
     */
    fun doOnNextFrame(onViewRendered: () -> Unit) {
        Choreographer.getInstance().postFrameCallback {
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.postAtFrontOfQueueAsync {
                onViewRendered.invoke()
            }
        }
    }

    fun Handler.postAtFrontOfQueueAsync(callback: () -> Unit) {
        sendMessageAtFrontOfQueue(Message.obtain(this, callback).apply {
            if (Build.VERSION.SDK_INT >= 22) {
                isAsynchronous = true
            }
        })
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





