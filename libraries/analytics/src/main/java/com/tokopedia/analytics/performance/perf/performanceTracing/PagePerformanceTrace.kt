package com.tokopedia.analytics.performance.perf.performanceTracing

import android.app.Activity
import android.view.View
import com.tokopedia.analytics.performance.perf.LoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.XmlPerformanceGlobalLayoutParser
import com.tokopedia.analytics.performance.perf.performanceTracing.repository.PerformanceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference

class PagePerformanceTrace(
    val activity: Activity,
    val performanceRepository: PerformanceRepository,
    val loadableComponentFlow: MutableSharedFlow<LoadableComponent>,
    val onPerformanceTraceFinished: (Success<PerformanceTraceData>) -> Unit,
    val onPerformanceTraceError: (Error) -> Unit
) : PerformanceTrace {

    protected val scope =
        CoroutineScope(Dispatchers.Main + Job())

    private var startCurrentTimeMillis = System.currentTimeMillis()
    private var performanceTraceData: AtomicReference<PerformanceTraceData> =
        AtomicReference(PerformanceTraceData())
    private var performanceBlocks = AtomicReference(mutableMapOf<String, BlocksModel>())

    private var trace_id: String = ""
    init {
        scope.launch {
            loadableComponentFlow.collect { loadableComponent ->
                val currentPerformanceBlocks = performanceBlocks.get()

                if (currentPerformanceBlocks.containsKey(loadableComponent.name())) {
                    val blockModel = currentPerformanceBlocks.get(loadableComponent.name())
                    blockModel?.let { block ->
                        currentPerformanceBlocks.put(
                            loadableComponent.name(),
                            block.copy(
                                endTime = System.currentTimeMillis()
                            )
                        )
                    }
                } else {
                    val startTime = System.currentTimeMillis()
                    currentPerformanceBlocks.put(
                        loadableComponent.name(),
                        BlocksModel(
                            name = loadableComponent.name(),
                            startTime = startTime
                        )
                    )
                }
            }
        }
    }

    override fun traceId(): String {
        return trace_id
    }

    override fun setTraceId(id: String) {
        this.trace_id = id
    }
    override fun startMonitoring() {
        performanceRepository.startRecord()
        val rootView = activity.window.decorView.findViewById<View>(android.R.id.content)
        val globalLayoutListener = XmlPerformanceGlobalLayoutParser(
            rootView = rootView
        ) {
            if (!performanceTraceData.get().ttilMeasured()) {
                recordTTIL()
                stopMonitoring(Success(performanceTraceData.get()))
            }

            if (!performanceTraceData.get().ttflMeasured()) {
                recordTTFL()
            }
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    override fun stopMonitoring(result: Result<PerformanceTraceData>) {
        performanceRepository.stopRecord(performanceBlocks.get())
        when (result) {
            is Success -> onPerformanceTraceFinished.invoke(result)
            is Error -> onPerformanceTraceError.invoke(result)
        }
    }

    override fun recordPerformanceData(performanceTraceData: PerformanceTraceData) {
    }

    private fun recordTTIL() {
        val elapsedTime = System.currentTimeMillis() - startCurrentTimeMillis
        performanceTraceData.set(
            performanceTraceData.get().copy(timeToInitialLoad = elapsedTime)
        )
    }

    private fun recordTTFL() {
        val elapsedTime = System.currentTimeMillis() - startCurrentTimeMillis
        performanceTraceData.set(
            performanceTraceData.get().copy(timeToFirstLayout = elapsedTime)
        )
    }
}
