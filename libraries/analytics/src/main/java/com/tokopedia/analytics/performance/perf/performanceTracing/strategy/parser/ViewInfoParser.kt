package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser

import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.ViewInfo

interface ViewInfoParser<T> {
    fun parse(input: T, depth: Int): List<ViewInfo>
}
