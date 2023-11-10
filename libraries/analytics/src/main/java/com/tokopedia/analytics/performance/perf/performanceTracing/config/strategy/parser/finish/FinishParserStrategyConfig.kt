package com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.finish

import com.tokopedia.analytics.performance.perf.performanceTracing.config.ViewInfo

interface FinishParserStrategyConfig<T> {
    fun isLayoutFinished(rootView: T, viewInfos: List<ViewInfo>): Boolean
}
