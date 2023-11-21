package com.tokopedia.analytics.performance.perf.performanceTracing.strategy

import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.callback.ViewCallbackStrategy
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.finish.FinishParserStrategyConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.start.StartParserStrategyConfig

interface ParsingStrategy<T> {
    fun getStartParserStrategy(): StartParserStrategyConfig<T>

    fun getFinishParsingStrategry(): FinishParserStrategyConfig<T>

    fun getViewCallbackStrategy(): ViewCallbackStrategy
}
