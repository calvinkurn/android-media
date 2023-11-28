package com.tokopedia.analytics.performance.perf.performanceTracing.trace

import android.app.Activity
import android.view.View
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.ParsingStrategy
import com.tokopedia.analytics.performance.perf.performanceTracing.data.BlocksModel
import com.tokopedia.analytics.performance.perf.performanceTracing.data.PerformanceTraceData
import com.tokopedia.analytics.performance.perf.performanceTracing.repository.PerformanceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference

class XMLPagePerformanceTrace(
    val activity: Activity,
    val performanceRepository: PerformanceRepository,
    val loadableComponentFlow: MutableSharedFlow<LoadableComponent>,
    val parsingStrategy: ParsingStrategy<View>,
    val onPerformanceTraceFinished: (Success<PerformanceTraceData>) -> Unit,
    val onPerformanceTraceError: (Error) -> Unit
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
                activityName = activity.javaClass.simpleName,
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
        startTime = System.currentTimeMillis()
        observeLoadableComponent()

        var rootView = activity.window.decorView.findViewById<View>(android.R.id.content)
        observeXMLPagePerformance(rootView)
        
        performanceRepository.startRecord()
        parsingStrategy.getViewCallbackStrategy().startObserving()
    }

    override fun stopMonitoring(result: Result<PerformanceTraceData>) {
        recordPerformanceData(result)
        when (result) {
            is Success -> onPerformanceTraceFinished.invoke(result)
            is Error -> onPerformanceTraceError.invoke(result)
        }
        scope.cancel()
        isPerformanceTraceFinished = true
        parsingStrategy.getViewCallbackStrategy().stopObserving()
    }

    override fun recordPerformanceData(result: Result<PerformanceTraceData>) {
        when(result) {
            is Success -> {
                performanceRepository.stopRecord(
                    performanceTraceData.get().blocksModel
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
    
    private fun observeXMLPagePerformance(rootView: View) {
        parsingStrategy.getViewCallbackStrategy().registerCallback {
            
            if (isPerformanceTraceFinished) {
                parsingStrategy.getViewCallbackStrategy().stopObserving()
            }

            if (isTimeout()) {
                finishParsing(Error("Parsing timeout."))
            }

            if (perfParsingJob?.isActive == true) {
                perfParsingJob?.cancel()
            }

            perfParsingJob = scope.launch(Dispatchers.IO) {
                /**
                 * Parse root view into list of ViewInfo model
                 */
                val viewInfos = parsingStrategy.getViewInfoParserStrategy().parse(rootView, 0)

                if (viewInfos.isNotEmpty()) {
                    onLayoutRendered()
                }
                /**
                 * If the given root view is ready to parse, then start to validate ViewInfo
                 */
                if (parsingStrategy.getStartParserStrategy().isLayoutReady(rootView, viewInfos)) {
                    val isLayoutFinished = parsingStrategy.getFinishParsingStrategy().isLayoutFinished(rootView, viewInfos)

                    if (isLayoutFinished) {
                        scope.launch(Dispatchers.Main) {
                            finishParsing(Success(true))
                        }
                    }
                }

            }
        }
        
    }

    fun finishParsing(result: Result<Boolean>) {
        when (result) {
            is Success -> {
                if (!performanceTraceData.get().ttilMeasured()) {
                    recordTTIL()
                    stopMonitoring(Success(performanceTraceData.get()))
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
