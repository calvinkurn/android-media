package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.callback

import android.view.View

interface ViewCallbackStrategy {

    fun startObserving(view: View)
    fun registerCallback(onRender: () -> Unit)

    fun stopObserving(view: View)
}
