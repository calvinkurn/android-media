package com.tokopedia.analytics.performance.perf.performanceTracing.strategy

import android.view.View
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.callback.FrameViewCallbackStrategy
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.callback.ViewCallbackStrategy
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.condition.finish.FinishConditionStrategyConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.condition.finish.FullRecyclerViewPageFinishConditionStrategy
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.ViewInfoParser
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.XmlViewInfoParser

class RecyclerViewPageParsingStrategy() : ParsingStrategy<View> {
    val strategy = FrameViewCallbackStrategy()
    override fun getFinishParsingStrategy(): FinishConditionStrategyConfig<View> {
        return FullRecyclerViewPageFinishConditionStrategy()
    }

    override fun getViewCallbackStrategy(): ViewCallbackStrategy {
        return strategy
    }

    override suspend fun getViewInfoParserStrategy2(): ViewInfoParser<View> {
        return XmlViewInfoParser()
    }
}
