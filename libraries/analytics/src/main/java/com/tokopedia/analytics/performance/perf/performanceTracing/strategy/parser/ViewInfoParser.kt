package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser

import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.ViewInfo

interface ViewInfoParser<T> {
    suspend fun parse(input: T): ViewInfo
}
