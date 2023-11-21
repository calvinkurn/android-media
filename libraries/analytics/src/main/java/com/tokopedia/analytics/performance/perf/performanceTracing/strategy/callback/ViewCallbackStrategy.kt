package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.callback

interface ViewCallbackStrategy {

    fun startObserving()
    fun registerCallback(onRender: () -> Unit)

    fun stopObserving()
}
