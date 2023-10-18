package com.tokopedia.analytics.performance.perf

interface LoadableComponent {
    fun isLoading(): Boolean
    fun finishLoading()

    fun name(): String
}
