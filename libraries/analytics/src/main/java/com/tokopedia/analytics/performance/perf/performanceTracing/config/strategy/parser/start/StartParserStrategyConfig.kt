package com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.start

import com.tokopedia.analytics.performance.perf.performanceTracing.config.ViewInfo

interface StartParserStrategyConfig<T> {
    fun isLayoutReady(rootView: T, viewInfos: List<ViewInfo>): Boolean
}
