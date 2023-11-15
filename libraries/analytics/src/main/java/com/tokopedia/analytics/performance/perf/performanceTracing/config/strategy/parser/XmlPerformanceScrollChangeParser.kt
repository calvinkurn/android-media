package com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser

import android.view.View
import android.view.ViewTreeObserver
import com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.finish.FinishParserStrategyConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.finish.FullRecyclerViewPageFinishParserStrategy
import com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.start.FullRecyclerViewPageStartParser
import com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.start.StartParserStrategyConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class XmlPerformanceScrollChangeParser(
    val rootView: View,
    val viewInfoParser: ViewInfoParser<View> = XmlViewInfoParser(),
    val startParserStrategyConfig: StartParserStrategyConfig<View> = FullRecyclerViewPageStartParser(),
    val finishParserStartegyConfig: FinishParserStrategyConfig<View> = FullRecyclerViewPageFinishParserStrategy(),
    val onLayoutRendered: () -> Unit,
    val onLayoutFinished: () -> Unit
) : ViewTreeObserver.OnScrollChangedListener {
    protected val scope =
        CoroutineScope(Dispatchers.Main + Job())

    var perfParsingJob: Job? = null
    var isPerformanceTraceFinished = false

    override fun onScrollChanged() {
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

                if (isLayoutFinished) {
                    scope.launch(Dispatchers.Main) {
                        rootView.post {
                            onLayoutFinished.invoke()
                            scope.cancel()
                            isPerformanceTraceFinished = true
                        }
                    }
                }
            }
        }
    }

    private fun tearDownListener() {
        rootView.viewTreeObserver.removeOnScrollChangedListener(this)
    }
}
