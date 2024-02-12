package com.tokopedia.analytics.performance.perf.performanceTracing.trace

import android.util.Log
import android.view.View
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.data.BlocksModel
import com.tokopedia.analytics.performance.perf.performanceTracing.data.PerformanceTraceData
import com.tokopedia.analytics.performance.perf.performanceTracing.repository.PerformanceRepository
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.ParsingStrategy
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.ViewInfo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.util.concurrent.atomic.AtomicReference

class XMLPagePerformanceTrace(
    val activityName: String,
    val rootView: View,
    val performanceRepository: PerformanceRepository,
    val loadableComponentFlow: MutableSharedFlow<LoadableComponent>,
    val parsingStrategy: ParsingStrategy<View>,
    var onPerformanceTraceFinished: ((Success<PerformanceTraceData>) -> Unit)?,
    var onPerformanceTraceError: ((Error) -> Unit)?
) : PerformanceTrace {

    companion object {
        private const val DEFAULT_TIMEOUT = 10000
    }

    protected val scope =
        CoroutineScope(Dispatchers.Main + Job())

    private var startCurrentTimeMillis = System.currentTimeMillis()

    private var trace_id: String = ""
    private var performanceTraceData: AtomicReference<PerformanceTraceData> =
        AtomicReference(
            PerformanceTraceData(
                activityName = activityName,
                traceName = performanceRepository.getTraceName()
            )
        )
    var isPerformanceTraceFinished = false
    var perfParsingJob: Job? = null
    var startTime = System.currentTimeMillis()

    override fun traceId(): String {
        return trace_id
    }

    override fun setTraceId(id: String) {
        this.trace_id = id
    }
    override fun startMonitoring() {
        performanceRepository.startRecord()
        startTime = System.currentTimeMillis()
        observeLoadableComponent()

        observeXMLPagePerformance(rootView)

        parsingStrategy.getViewCallbackStrategy().startObserving(rootView.context)
    }

    override fun stopMonitoring(result: Result<PerformanceTraceData>) {
        when (result) {
            is Success -> onPerformanceTraceFinished?.invoke(result)
            is Error -> onPerformanceTraceError?.invoke(result)
            else -> {}
        }
        scope.cancel()
        isPerformanceTraceFinished = true
        onPerformanceTraceFinished = null
        onPerformanceTraceError = null
        parsingStrategy.getViewCallbackStrategy().stopObserving(rootView.context)
    }

    override fun recordPerformanceData(result: Result<PerformanceTraceData>) {
        when (result) {
            is Success -> {
                performanceRepository.stopRecord(
                    performanceTraceData.get()
                )
            }
            else -> {}
        }
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
        onPerformanceTraceFinished?.invoke(Success(performanceTraceData.get(), "TTFL finished"))
    }

    private fun observeLoadableComponent() {
        scope.launch {
            loadableComponentFlow.collect { loadableComponent ->
                val currentPerformanceData = performanceTraceData.get()
                val currentPerformanceBlocks = currentPerformanceData.blocksModel.toMutableMap()

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
                            startTraceTime = startCurrentTimeMillis,
                            startTime = startTime
                        )
                    )
                }
                performanceTraceData.set(
                    currentPerformanceData.copy(
                        blocksModel = currentPerformanceBlocks
                    )
                )
            }
        }
    }

    @Suppress("SwallowedException")
    private fun observeXMLPagePerformance(rootView: View) {
        parsingStrategy.getViewCallbackStrategy().registerCallback {
            if (isPerformanceTraceFinished) {
                perfParsingJob?.cancel()
            }

            if (isTimeout()) {
                finishParsing(Error(parsingStrategy.getFinishParsingStrategy().timeoutMessage()))
            }

            if (perfParsingJob?.isActive == true) {
                perfParsingJob?.cancel()
            }

            Log.d("PerfThread", "Frame Callback Received")

            perfParsingJob = scope.launch(
                Dispatchers.Default + CoroutineName("PerfParse")
            ) {
                val threadName = "${Thread.currentThread().name}"
                Log.d("PerfThread", "Starting parse job in thread $threadName")

                try {
                    yield()
                    val viewInfos = try {
                        parsingStrategy.getViewInfoParserStrategy2().parse(rootView)
                    } catch (e: CancellationException) {
                        ViewInfo()
                    }
                    if (viewInfos.directChilds.isNotEmpty()) {
                        onLayoutRendered()
                    }
                    val layoutStatus = parsingStrategy.getFinishParsingStrategy().isLayoutFinished(rootView, viewInfos)

                    if (layoutStatus.isFinishedLoading) {
                        scope.launch(Dispatchers.Main) {
                            finishParsing(Success(true, layoutStatus.summary))
                        }
                    }
                } catch (e: CancellationException) {
                    Log.d("PerfThread", "Cancelled in thread $threadName")
                }
            }
        }
    }

    fun finishParsing(result: Result<Boolean>) {
        when (result) {
            is Success -> {
                if (!performanceTraceData.get().ttilMeasured()) {
                    recordTTIL()
                    stopMonitoring(Success(performanceTraceData.get(), result.message))
                }
            }
            is Error -> {
                stopMonitoring(result)
            }
        }
    }

    private fun onLayoutRendered() {
        if (!performanceTraceData.get().ttflMeasured()) {
            recordTTFL()
        }
    }

    private fun isTimeout(): Boolean {
        return System.currentTimeMillis() - startTime >= DEFAULT_TIMEOUT
    }
}
