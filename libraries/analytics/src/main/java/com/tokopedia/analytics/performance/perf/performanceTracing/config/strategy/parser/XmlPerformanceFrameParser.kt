package com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser

import android.view.View
import com.tokopedia.analytics.performance.perf.performanceTracing.Error
import com.tokopedia.analytics.performance.perf.performanceTracing.Result
import com.tokopedia.analytics.performance.perf.performanceTracing.Success
import com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.finish.FinishParserStrategyConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.finish.FullRecyclerViewPageFinishParserStrategy
import com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.start.FullRecyclerViewPageStartParser
import com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.start.StartParserStrategyConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.util.FrameRenderCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class XmlPerformanceFrameParser(
    val rootView: View,
    val viewInfoParser: ViewInfoParser<View> = XmlViewInfoParser(),
    val startParserStrategyConfig: StartParserStrategyConfig<View> = FullRecyclerViewPageStartParser(),
    val finishParserStartegyConfig: FinishParserStrategyConfig<View> = FullRecyclerViewPageFinishParserStrategy(),
    val onLayoutRendered: () -> Unit,
    val onLayoutFinished: (Result<Boolean>) -> Unit
) {

    companion object {
        private const val DEFAULT_TIMEOUT = 10000
    }
    protected val scope =
        CoroutineScope(Dispatchers.Main + Job())

    var perfParsingJob: Job? = null
    var isPerformanceTraceFinished = false

    var frameRenderCallback: FrameRenderCallback? = null

    var startTime = System.currentTimeMillis()
    init {
        frameRenderCallback = FrameRenderCallback {
            if (isPerformanceTraceFinished) {
                tearDownListener()
            }

            if (isTimeout()) {
                finishParsing(false, "Parsing timeout.")
            }

            if (perfParsingJob?.isActive == true) {
                perfParsingJob?.cancel()
            }

            perfParsingJob = scope.launch(Dispatchers.IO) {
                onLayoutRendered.invoke()
                /**
                 * Parse root view into list of ViewInfo model
                 */
                val viewInfos = viewInfoParser.parse(rootView, 0)

                /**
                 * If the given root view is ready to parse, then start to validate ViewInfo
                 */
                if (startParserStrategyConfig.isLayoutReady(rootView, viewInfos)) {
                    val isLayoutFinished = finishParserStartegyConfig.isLayoutFinished(rootView, viewInfos)

                    if (isLayoutFinished) {
                        scope.launch(Dispatchers.Main) {
                            finishParsing(true)
                        }
                    }
                }
            }
        }
        frameRenderCallback?.start()
    }

    fun finishParsing(success: Boolean, message: String = "") {
        if (success) {
            onLayoutFinished.invoke(Success(success))
        } else {
            onLayoutFinished.invoke(Error(message))
        }
        scope.cancel()
        isPerformanceTraceFinished = true
        tearDownListener()
    }

    private fun tearDownListener() {
        frameRenderCallback?.stop()
    }

    private fun isTimeout(): Boolean {
        return System.currentTimeMillis() - startTime >= DEFAULT_TIMEOUT
    }
}
