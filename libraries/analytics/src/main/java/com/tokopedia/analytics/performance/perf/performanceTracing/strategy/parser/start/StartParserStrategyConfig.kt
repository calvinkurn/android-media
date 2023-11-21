package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.start

import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.ViewInfo

interface StartParserStrategyConfig<T> {
    fun isLayoutReady(rootView: T, viewInfos: List<ViewInfo>): Boolean
}
