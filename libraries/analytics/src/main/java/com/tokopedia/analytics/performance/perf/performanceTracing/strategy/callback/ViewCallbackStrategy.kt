package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.callback

import android.content.Context

interface ViewCallbackStrategy {

    fun startObserving(context: Context)
    fun registerCallback(onRender: () -> Unit)

    fun stopObserving(context: Context)
}
