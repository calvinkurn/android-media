package com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser

import android.view.View
import android.view.ViewTreeObserver
import com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.finish.DefaultFinishParserStrategy
import com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.finish.FinishParserStrategyConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.start.DefaultStartParserStrategy
import com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.start.StartParserStrategyConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class XmlPerformanceGlobalLayoutParser(
    val rootView: View,
    val viewInfoParser: ViewInfoParser<View> = XmlViewInfoParser(),
    val startParserStrategyConfig: StartParserStrategyConfig<View> = DefaultStartParserStrategy(),
    val finishParserStartegyConfig: FinishParserStrategyConfig<View> = DefaultFinishParserStrategy(),
    val onLayoutRendered: () -> Unit,
    val onLayoutFinished: () -> Unit
) : ViewTreeObserver.OnGlobalLayoutListener {
    protected val scope =
        CoroutineScope(Dispatchers.Main + Job())

    var perfParsingJob: Job? = null
    var isPerformanceTraceFinished = false

    var finishDrawListener = object : ViewTreeObserver.OnDrawListener {
        override fun onDraw() {
            onLayoutFinished.invoke()
            scope.cancel()
            isPerformanceTraceFinished = true
        }
    }
    override fun onGlobalLayout() {
        if (isPerformanceTraceFinished) {
            tearDownListener()
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
            yield()

            /**
             * If the given root view is ready to parse, then start to validate ViewInfo
             */
            if (startParserStrategyConfig.isLayoutReady(rootView, viewInfos)) {
                val isLayoutFinished = finishParserStartegyConfig.isLayoutFinished(rootView, viewInfos)
                /**
                 * If the list of ViewInfo is already met the requirement of FinishParserStrategy,
                 * then add finishDrawListener to stop monitoring Performance Trace
                 * on the next onDraw call when view actually rendered
                 */
                if (isLayoutFinished) {
                    scope.launch(Dispatchers.Main) {
                        rootView.viewTreeObserver.addOnDrawListener(finishDrawListener)
                    }
                }
            }
        }
    }

    private fun tearDownListener() {
        rootView.viewTreeObserver.removeOnGlobalLayoutListener(this@XmlPerformanceGlobalLayoutParser)
        rootView.viewTreeObserver.removeOnDrawListener(finishDrawListener)
    }
}
