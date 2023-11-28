package com.tokopedia.analytics.performance.perf.performanceTracing.strategy

import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.callback.ViewCallbackStrategy
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.ViewInfoParser
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.finish.FinishParserStrategyConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.start.StartParserStrategyConfig

interface ParsingStrategy<T> {
    fun getStartParserStrategy(): StartParserStrategyConfig<T>

    fun getFinishParsingStrategy(): FinishParserStrategyConfig<T>

    fun getViewCallbackStrategy(): ViewCallbackStrategy
    
    fun getViewInfoParserStrategy(): ViewInfoParser<T>
}
