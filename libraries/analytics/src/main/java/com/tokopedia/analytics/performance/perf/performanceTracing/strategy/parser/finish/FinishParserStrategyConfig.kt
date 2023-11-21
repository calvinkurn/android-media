package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.finish

import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.ViewInfo

interface FinishParserStrategyConfig<T> {
    fun isLayoutFinished(rootView: T, viewInfos: List<ViewInfo>): Boolean
}
