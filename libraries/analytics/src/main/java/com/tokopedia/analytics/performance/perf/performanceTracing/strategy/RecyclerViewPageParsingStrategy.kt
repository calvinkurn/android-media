package com.tokopedia.analytics.performance.perf.performanceTracing.strategy

import android.view.View
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.callback.FrameViewCallbackStrategy
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.callback.ViewCallbackStrategy
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.ViewInfoParser
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.XmlViewInfoParser
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.finish.FinishParserStrategyConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.finish.FullRecyclerViewPageFinishParserStrategy
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.start.FullRecyclerViewPageStartParser
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.start.StartParserStrategyConfig

class RecyclerViewPageParsingStrategy : ParsingStrategy<View> {

    val strategy: ViewCallbackStrategy = FrameViewCallbackStrategy()
    override fun getStartParserStrategy(): StartParserStrategyConfig<View> {
        return FullRecyclerViewPageStartParser()
    }

    override fun getFinishParsingStrategy(): FinishParserStrategyConfig<View> {
        return FullRecyclerViewPageFinishParserStrategy()
    }

    override fun getViewCallbackStrategy(): ViewCallbackStrategy {
        return strategy
    }

    override fun getViewInfoParserStrategy(): ViewInfoParser<View> {
        return XmlViewInfoParser()
    }
}
