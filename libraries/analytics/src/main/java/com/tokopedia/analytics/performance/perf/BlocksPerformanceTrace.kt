package com.tokopedia.analytics.performance.perf

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Choreographer
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import com.tokopedia.abstraction.base.view.listener.TouchListenerActivity
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.analytics.performance.perf.PerformanceTraceDebugger.takeScreenshot
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_PERFORMANCE_TRACE
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.internal.toImmutableList
import java.util.concurrent.atomic.AtomicReference

@FlowPreview
class BlocksPerformanceTrace(
    @ApplicationContext context: Context?,
    traceName: String,
    scope: LifecycleCoroutineScope,
    ) {
    companion object {
        const val TYPE_TTIL = "TTIL"
        const val ATTR_CONDITION = "State"
        const val ATTR_BLOCKS = "Blocks"

        const val FINISHED_LOADING_TTFL_BLOCKS_THRESHOLD = 1
        const val FINISHED_LOADING_TTIL_BLOCKS_THRESHOLD = 5
    }
    private var startCurrentTimeMillis = 0L
    private var currentLoadableComponentList = mutableListOf<LoadableComponent>()

    private var summaryModel: AtomicReference<BlocksSummaryModel> = AtomicReference(BlocksSummaryModel())
    private var TTFLperformanceMonitoring: PerformanceMonitoring? = PerformanceMonitoring()
    private var TTILperformanceMonitoring: PerformanceMonitoring? = PerformanceMonitoring()

    private var onLaunchTimeFinished: ((BlocksSummaryModel, capturedBlocks: List<LoadableComponent>) -> Unit)? = {
            summaryModel: BlocksSummaryModel, capturedBlocks: List<LoadableComponent> -> }

    private var performanceTraceJob: Job? = null

    private var listOfFinishedLoadableComponent = listOf<LoadableComponent>()

    private var ttflMeasured = false
    private var ttilMeasured = false

    private var isPerformanceTraceEnabled = true
    private var currentView: View? = null

    private var performanceBlocks = mutableListOf<LoadableComponent>()
    private var lastPerformanceBlocks = mutableListOf<LoadableComponent>()

    enum class BlocksPerfState {
        STATE_ERROR,
        STATE_PARTIALLY_ERROR,
        STATE_TIMEOUT,
        STATE_TOUCH,
        STATE_SUCCESS,
        STATE_ONPAUSED
    }
    init {
        context?.let {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            this.isPerformanceTraceEnabled = remoteConfig.getBoolean(
                ENABLE_PERFORMANCE_TRACE, true
            )
        }

        if (isPerformanceTraceEnabled) {
            startCurrentTimeMillis = System.currentTimeMillis()
            TTFLperformanceMonitoring?.startTrace("ttfl_perf_trace_$traceName")
            TTILperformanceMonitoring?.startTrace("ttil_perf_trace_$traceName")

            performanceTraceJob = scope.launch(Dispatchers.IO) {
                blocksPerformanceFlow().collect {
                    updateBlocks(it)
                }
            }
        }
    }

    fun init(
        v: View,
        touchListenerActivity: TouchListenerActivity?,
        onLaunchTimeFinished: (summaryModel: BlocksSummaryModel, capturedBlocks: List<LoadableComponent>) -> Unit =
            { _,_ -> },
    ) {
        this.currentView = v
        this.onLaunchTimeFinished = onLaunchTimeFinished
        if (isPerformanceTraceEnabled) {
            touchListenerActivity?.addListener {
                if (!ttflMeasured) {
                    finishTTFL(BlocksPerfState.STATE_TOUCH)
                }
                if (!ttilMeasured) {
                    finishTTIL(BlocksPerfState.STATE_TOUCH)
                }
            }
        }
    }

    fun debugPerformanceTrace(activity: Activity?, summaryModel: BlocksSummaryModel, type: String, view: View) {
        activity?.takeScreenshot(type, view)
        Toaster.build(view, "" +
            "TTFL: ${summaryModel.timeToFirstLayout?.inflateTime} ms \n" +
            "TTIL: ${summaryModel.timeToInitialLayout?.inflateTime} ms \n" ).show()
    }

    fun addViewPerformanceBlocks(view: View?) {
        view?.let { performanceBlocks.addPerformanceBlocks(view) }
    }

    fun setLoadableComponentListPerformanceBlocks(positionToAdd: Int = 0, listOfLoadableComponent: List<LoadableComponent>) {
        viewHierarchyInUsableState {
            val tempBlocks = performanceBlocks
            tempBlocks.subList(positionToAdd, performanceBlocks.size).clear()
            tempBlocks.addAll(positionToAdd, listOfLoadableComponent)

            performanceBlocks = tempBlocks
        }
    }

    fun finishOnPaused() {
        finishTTIL(BlocksPerfState.STATE_ONPAUSED)
        finishTTFL(BlocksPerfState.STATE_ONPAUSED)
    }

    fun cancelPerformanceTrace(state: BlocksPerfState, targetPerfMonitoring: PerformanceMonitoring? = null, listOfFinishedLoadableComponent: List<LoadableComponent> = listOf()) {
        targetPerfMonitoring?.putCustomAttribute(
            ATTR_CONDITION, state.name)
        targetPerfMonitoring?.putCustomAttribute(
            ATTR_BLOCKS, listOfFinishedLoadableComponent.map { it.name() to "isLoading:${it.isLoading()}" }.joinToString(
                prefix = "[",
                separator = ", ",
                postfix = "]",
                truncated = "..."
            ))
        targetPerfMonitoring?.stopTrace()

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

    fun setPageState(state: BlocksPerfState) {
        TTFLperformanceMonitoring?.putCustomAttribute(
            ATTR_CONDITION, state.name)
        TTILperformanceMonitoring?.putCustomAttribute(
            ATTR_CONDITION, state.name)
    }

    private fun blocksPerformanceFlow() : Flow<List<LoadableComponent>> {
        return flow {
            while(true) {
                val tempPerfBlocks = performanceBlocks.toImmutableList()
                val tempLastPerfBlocks = lastPerformanceBlocks.toImmutableList()

                if (tempPerfBlocks.size != tempLastPerfBlocks.size) {
                    lastPerformanceBlocks = performanceBlocks.toMutableList()
                    emit(performanceBlocks)
                }
            }
        }
    }

    private fun updateBlocks(objList: List<Any>) {
        currentLoadableComponentList = objList.filterIsInstance<LoadableComponent>().toMutableList()
        listOfFinishedLoadableComponent = currentLoadableComponentList.filter { !it.isLoading() }

        if (!ttflMeasured && TTFLperformanceMonitoring != null && listOfFinishedLoadableComponent.size >= FINISHED_LOADING_TTFL_BLOCKS_THRESHOLD) {
            measureTTFL(listOfFinishedLoadableComponent)
        }
        if (!ttilMeasured && TTILperformanceMonitoring != null && listOfFinishedLoadableComponent.size >= FINISHED_LOADING_TTIL_BLOCKS_THRESHOLD) {
            measureTTIL(listOfFinishedLoadableComponent)
        }
    }

    private fun measureTTIL(listOfLoadableComponent: List<LoadableComponent>) {
        val blocksModel = createBlocksPerformanceModel()
        validateTTIL(blocksModel)
        onLaunchTimeFinished?.invoke(
            summaryModel.get(), listOfFinishedLoadableComponent
        )
        finishTTIL(BlocksPerfState.STATE_SUCCESS, listOfLoadableComponent)
        this.onLaunchTimeFinished = null
        TTILperformanceMonitoring = null
        performanceTraceJob?.cancel()
    }

    private fun measureTTFL(listOfLoadableComponent: List<LoadableComponent>) {
        val blocksModel = createBlocksPerformanceModel()
        validateTTFL(blocksModel)
        onLaunchTimeFinished?.invoke(
            summaryModel.get(), listOfFinishedLoadableComponent
        )
        finishTTFL(BlocksPerfState.STATE_SUCCESS, listOfLoadableComponent)
        TTFLperformanceMonitoring = null
    }


    private fun finishTTIL(state: BlocksPerfState, listOfLoadableComponent: List<LoadableComponent> = listOf()) {
        cancelPerformanceTrace(state, TTILperformanceMonitoring, listOfLoadableComponent)
        ttilMeasured = true
    }

    private fun finishTTFL(state: BlocksPerfState, listOfLoadableComponent: List<LoadableComponent> = listOf()) {
        cancelPerformanceTrace(state, TTFLperformanceMonitoring, listOfLoadableComponent)
        ttflMeasured = true
    }

    private fun createBlocksPerformanceModel() = BlocksPerformanceModel(
        inflateTime = System.currentTimeMillis() - startCurrentTimeMillis,
        capturedBlocks = listOfFinishedLoadableComponent.map { it.name() }
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


fun MutableList<LoadableComponent>.addPerformanceBlocks(v: View?) {
    viewHierarchyInUsableState {
        this.add(object: LoadableComponent by BlocksLoadableComponent(
            { true },
            v?.javaClass?.simpleName
        ) {})
    }
}

/**
 * Helper function to validate if a view is already rendered on screen as a frame
 */
fun viewHierarchyInUsableState(doAfterRender: () -> Unit) {
    Choreographer.getInstance().postFrameCallback {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.postAtFrontOfQueueAsync {
            doAfterRender.invoke()
        }
    }
}
fun Handler.postAtFrontOfQueueAsync(callback: () -> Unit) {
    sendMessageAtFrontOfQueue((Message.obtain(this, callback).apply {
        if (Build.VERSION.SDK_INT >= 22) {
            isAsynchronous = true
        }
    }))
}



