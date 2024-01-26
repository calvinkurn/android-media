package com.tokopedia.analytics.performance.perf.performanceTracing.components

interface LoadableComponent {
    fun isLoading(): Boolean
    fun finishLoading()

    fun name(): String
}
