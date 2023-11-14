package com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser

import com.tokopedia.analytics.performance.perf.performanceTracing.config.ViewInfo

interface ViewInfoParser<T> {
    fun parse(input: T, depth: Int): List<ViewInfo>
}
