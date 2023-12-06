package com.tokopedia.analytics.performance.perf.performanceTracing.strategy

import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.callback.ViewCallbackStrategy
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.ViewInfoParser
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.condition.finish.FinishConditionStrategyConfig

interface ParsingStrategy<T> {
    fun getFinishParsingStrategy(): FinishConditionStrategyConfig<T>

    fun getViewCallbackStrategy(): ViewCallbackStrategy
    
    suspend fun getViewInfoParserStrategy2(): ViewInfoParser<T>
}
