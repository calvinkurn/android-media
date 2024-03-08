package com.tokopedia.analytics.performance.perf

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.Trace
import android.util.Log
import android.view.Choreographer
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import com.tokopedia.abstraction.base.view.listener.TouchListenerActivity
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.iris.IrisPerformanceData
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_PERFORMANCE_TRACE
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference

@FlowPreview
class BlocksPerformanceTrace(
    context: Context?,
    val traceName: String,
    scope: LifecycleCoroutineScope,
    val touchListenerActivity: TouchListenerActivity?,
    val onPerformanceTraceCancelled: ((state: BlocksPerfState) -> Unit)? = null,
    onLaunchTimeFinished: ((summaryModel: BlocksSummaryModel, capturedBlocks: Set<String>) -> Unit)? = null
) {
    companion object {
        const val TYPE_TTIL = "TTIL"
        const val TYPE_TTFL = "TTFL"

        const val ATTR_CONDITION = "State"
        const val ATTR_BLOCKS = "Blocks"

        const val FINISHED_LOADING_TTFL_BLOCKS_THRESHOLD = 1
        const val FINISHED_LOADING_TTIL_BLOCKS_THRESHOLD = 3

        const val ANDROID_TRACE_FULLY_DRAWN = "reportFullyDrawn() for %s"

        const val COOKIE_TTFL = 77
        const val COOKIE_TTIL = 78
    }

    private var startCurrentTimeMillis = 0L

    private var summaryModel: AtomicReference<BlocksSummaryModel> =
        AtomicReference(BlocksSummaryModel())
    private var pagePerformanceMonitoring: PerformanceMonitoring? = PerformanceMonitoring()
    private var TTFLperformanceMonitoring: PerformanceMonitoring? = PerformanceMonitoring()
    private var TTILperformanceMonitoring: PerformanceMonitoring? = PerformanceMonitoring()

    private var performanceTraceJob: Job? = null

    private var ttflMeasured = false
    private var ttilMeasured = false
    private var appContext: Context? = null
    private var onLaunchTimeFinished: ((summaryModel: BlocksSummaryModel, capturedBlocks: Set<String>) -> Unit)? = null

    private var isPerformanceTraceEnabled = true

    private var performanceBlocks = mutableSetOf<String>()
    private var atomicPerformanceBlocks = AtomicReference(performanceBlocks)

    private var inputFlow =
        MutableSharedFlow<Set<String>>(1, onBufferOverflow = BufferOverflow.SUSPEND)

    private var perfBlockFlow = inputFlow.transform {
        val performanceBlocksTemp = atomicPerformanceBlocks.get()
        performanceBlocksTemp.addAll(it)
        atomicPerformanceBlocks.set(performanceBlocksTemp)

        emit(performanceBlocksTemp.size)
    }.stateIn(scope, SharingStarted.WhileSubscribed(5000), 0)

    var onBlocksRendered: ((summaryModel: BlocksSummaryModel, capturedBlocks: Set<String>, elapsedTime: Long, identifier: String) -> Unit)? = null

    enum class BlocksPerfState {
        STATE_ERROR,
        STATE_PARTIALLY_ERROR,
        STATE_TIMEOUT,
        STATE_TOUCH,
        STATE_SUCCESS,
        STATE_ONPAUSED
    }

    init {
        context?.applicationContext?.let {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            this.isPerformanceTraceEnabled = remoteConfig.getBoolean(
                ENABLE_PERFORMANCE_TRACE,
                true
            )
        }
        initialize(context, scope, onLaunchTimeFinished)
        Log.d("BlocksTrace", "Start...")
    }

    private fun initialize(
        context: Context?,
        scope: LifecycleCoroutineScope,
        onLaunchTimeFinished: ((summaryModel: BlocksSummaryModel, capturedBlocks: Set<String>) -> Unit)?
    ) {
        if (context == null) {
            return
        }
        appContext = context.applicationContext
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        this.isPerformanceTraceEnabled = remoteConfig.getBoolean(
            ENABLE_PERFORMANCE_TRACE,
            true
        )

        if (isPerformanceTraceEnabled) {
            startCurrentTimeMillis = System.currentTimeMillis()
            beginAsyncSystraceSection(
                "PageLoadTime.AsyncTTFL$traceName",
                COOKIE_TTFL
            )
            beginAsyncSystraceSection(
                "PageLoadTime.AsyncTTIL$traceName",
                COOKIE_TTIL
            )
            pagePerformanceMonitoring?.startTrace("block_perf_trace_$traceName")
            TTFLperformanceMonitoring?.startTrace("ttfl_perf_trace_$traceName")
            TTILperformanceMonitoring?.startTrace("ttil_perf_trace_$traceName")

            performanceTraceJob = scope.launch(Dispatchers.IO) {
                perfBlockFlow.collect {
                    if (!ttflMeasured && TTFLperformanceMonitoring != null && it >= FINISHED_LOADING_TTFL_BLOCKS_THRESHOLD) {
                        measureTTFL(atomicPerformanceBlocks.get())
                        endAsyncSystraceSection("PageLoadTime.AsyncTTFL$traceName", COOKIE_TTFL)
                    }

                    if (!ttilMeasured && TTILperformanceMonitoring != null && it >= FINISHED_LOADING_TTIL_BLOCKS_THRESHOLD) {
                        measureTTIL(atomicPerformanceBlocks.get())
                        endAsyncSystraceSection("PageLoadTime.AsyncTTIL$traceName", COOKIE_TTIL)
                        scope.launch(Dispatchers.Main) {
                            putFullyDrawnTrace(traceName)
                        }
                    }
                }
            }
            touchListenerActivity?.addListener {
                if (!ttflMeasured) {
                    finishTTFL(BlocksPerfState.STATE_TOUCH)
                }
                if (!ttilMeasured) {
                    finishTTIL(BlocksPerfState.STATE_TOUCH)
                }
            }
            this.onLaunchTimeFinished = onLaunchTimeFinished
        }
    }

    fun beginAsyncSystraceSection(methodName: String, cookie: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Trace.beginAsyncSection(methodName, cookie)
        }
    }

    fun endAsyncSystraceSection(methodName: String, cookie: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Trace.endAsyncSection(methodName, cookie)
        }
    }

    private fun putFullyDrawnTrace(traceName: String) {
        try {
            Trace.beginSection(
                String.format(
                    ANDROID_TRACE_FULLY_DRAWN,
                    traceName
                )
            )
        } finally {
            Trace.endSection()
        }
    }

    fun debugPerformanceTrace(
        activity: Activity?,
        summaryModel: BlocksSummaryModel,
        type: String,
        view: View
    ) {
//        activity?.takeScreenshot(type, view)
        Toaster.build(
            view,
            "" +
                "TTFL: ${summaryModel.timeToFirstLayout?.inflateTime} ms \n" +
                "TTIL: ${summaryModel.timeToInitialLayout?.inflateTime} ms \n"
        ).show()
    }

    fun addViewPerformanceBlocks(view: View?) {
        view?.let {
            atomicPerformanceBlocks.get().addPerformanceBlocks(view) {
                inputFlow.tryEmit(atomicPerformanceBlocks.get())
            }
        }
    }

    fun setBlock(list: List<Any>, identifier: String = "", blockLimit: Int = 0) {
        if (list.allLoadableComponentFinished() && list.size >= blockLimit) {
            setLoadableComponentListPerformanceBlocks(
                list.getFinishedLoadableComponent()
            )
        }
        val allFinishedLoadableComponent = list.filter { it is LoadableComponent && !it.isLoading() }
        viewHierarchyInUsableState {
            val currentElapsedTime = System.currentTimeMillis() - startCurrentTimeMillis
            onBlocksRendered?.invoke(
                summaryModel.get(),
                allFinishedLoadableComponent.map {
                    (it as? LoadableComponent)?.name() ?: ""
                }.toSet(),
                currentElapsedTime,
                identifier
            )
        }
    }

    private fun setLoadableComponentListPerformanceBlocks(listOfLoadableComponent: List<String>) {
        viewHierarchyInUsableState {
            val tempBlocks = atomicPerformanceBlocks.get()
            tempBlocks.addAll(listOfLoadableComponent.toSet())

            atomicPerformanceBlocks.set(tempBlocks)

            inputFlow.tryEmit(tempBlocks)
        }
    }

    fun finishOnPaused() {
        if (!ttilMeasured) finishTTIL(BlocksPerfState.STATE_ONPAUSED)
        if (!ttflMeasured) finishTTFL(BlocksPerfState.STATE_ONPAUSED)
    }

    private fun cancelPerformanceTrace(
        state: BlocksPerfState,
        targetPerfMonitoring: PerformanceMonitoring? = null,
        listOfFinishedLoadableComponent: Set<String> = setOf()
    ) {
        targetPerfMonitoring?.putCustomAttribute(
            ATTR_CONDITION,
            state.name
        )
        val setOfLoadableComponent = listOfFinishedLoadableComponent.toMutableSet()
        val attr = setOfLoadableComponent.map { it }.joinToString(
            prefix = "[",
            separator = ", ",
            postfix = "]",
            truncated = "..."
        )

        targetPerfMonitoring?.putCustomAttribute(
            ATTR_BLOCKS,
            attr
        )
        targetPerfMonitoring?.stopTrace()

        onPerformanceTraceCancelled?.invoke(state)
        if (summaryModel.get().timeToInitialLayout != null) {
            PerformanceTraceDebugger.logTrace(
                "Performance TTFL trace finished."
            )
        } else {
            PerformanceTraceDebugger.logTrace(
                "Performance TTIL trace cancelled due to scroll event / error"
            )
        }

        if (state != BlocksPerfState.STATE_SUCCESS) {
            performanceTraceJob?.cancel()
        }
    }

    fun setPageState(state: BlocksPerfState) {
        TTFLperformanceMonitoring?.putCustomAttribute(
            ATTR_CONDITION,
            state.name
        )
        TTILperformanceMonitoring?.putCustomAttribute(
            ATTR_CONDITION,
            state.name
        )
    }

    private fun measureTTIL(listOfLoadableComponent: Set<String>) {
        val blocksModel = createBlocksPerformanceModel()
        validateTTIL(blocksModel)
        onLaunchTimeFinished?.invoke(
            summaryModel.get(),
            atomicPerformanceBlocks.get()
        )
        finishTTIL(BlocksPerfState.STATE_SUCCESS, listOfLoadableComponent)
        trackIris()
        trackFirebase()

        this.onLaunchTimeFinished = null
        TTILperformanceMonitoring = null
        performanceTraceJob?.cancel()
        Log.d("BlocksTrace", "TTIL: " + summaryModel.get().ttil())
    }

    private fun trackFirebase() {
        pagePerformanceMonitoring?.putMetric(
            TYPE_TTFL,
            summaryModel.get().ttfl()
        )
        pagePerformanceMonitoring?.putMetric(
            TYPE_TTIL,
            summaryModel.get().ttil()
        )
        pagePerformanceMonitoring?.stopTrace()
        pagePerformanceMonitoring = null
    }

    private fun trackIris() {
        val ctx = appContext ?: return
        val summaryModel = summaryModel.get()
        val ttfl = summaryModel.ttfl()
        val ttil = summaryModel.ttil()
        if (ttfl > 0 && ttil > 0) {
            IrisAnalytics.getInstance(ctx)
                .trackPerformance(IrisPerformanceData(traceName, ttfl, ttil))
        }
    }

    private fun measureTTFL(listOfLoadableComponent: Set<String>) {
        val blocksModel = createBlocksPerformanceModel()
        validateTTFL(blocksModel)
        onLaunchTimeFinished?.invoke(
            summaryModel.get(),
            atomicPerformanceBlocks.get()
        )
        finishTTFL(BlocksPerfState.STATE_SUCCESS, listOfLoadableComponent)
        TTFLperformanceMonitoring = null
    }

    private fun finishTTIL(state: BlocksPerfState, listOfLoadableComponent: Set<String> = setOf()) {
        cancelPerformanceTrace(state, TTILperformanceMonitoring, listOfLoadableComponent)
        ttilMeasured = true
    }

    private fun finishTTFL(state: BlocksPerfState, listOfLoadableComponent: Set<String> = setOf()) {
        cancelPerformanceTrace(state, TTFLperformanceMonitoring, listOfLoadableComponent)
        ttflMeasured = true
    }

    private fun createBlocksPerformanceModel() = BlocksPerformanceModel(
        inflateTime = System.currentTimeMillis() - startCurrentTimeMillis,
        capturedBlocks = atomicPerformanceBlocks.get()
    )

    private fun validateTTFL(blocksPerformanceModel: BlocksPerformanceModel?) {
        summaryModel.get()?.let { it ->
            summaryModel.set(
                it.copy(
                    timeToFirstLayout = blocksPerformanceModel
                )
            )
        }
    }

    private fun validateTTIL(blocksPerformanceModel: BlocksPerformanceModel?) {
        summaryModel.get()?.let {
            summaryModel.set(
                it.copy(
                    timeToInitialLayout = blocksPerformanceModel
                )
            )
        }
    }
}

internal fun MutableSet<String>.addPerformanceBlocks(v: View?, onView: () -> Unit) {
    viewHierarchyInUsableState {
        this.add((v?.javaClass?.canonicalName ?: "") + "-${v.hashCode()}")
        onView.invoke()
    }
}

internal fun List<Any>.getFinishedLoadableComponent(): MutableList<String> {
    return (
        this.filter {
            it is LoadableComponent && !it.isLoading()
        }.map {
            (it as? LoadableComponent)?.name() ?: ""
        }.toMutableList()
        )
}

internal fun List<Any>.allLoadableComponentFinished(): Boolean {
    return this.filter { it is LoadableComponent && it.isLoading() }
        .isEmpty()
}

/**
 * Helper function to validate if a view is already rendered on screen as a frame
 */
internal fun viewHierarchyInUsableState(doAfterRender: () -> Unit) {
    Choreographer.getInstance().postFrameCallback {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.postAtFrontOfQueueAsync {
            doAfterRender.invoke()
        }
    }
}

internal fun Handler.postAtFrontOfQueueAsync(callback: () -> Unit) {
    sendMessageAtFrontOfQueue(
        (
            Message.obtain(this, callback).apply {
                if (Build.VERSION.SDK_INT >= 22) {
                    isAsynchronous = true
                }
            }
            )
    )
}
