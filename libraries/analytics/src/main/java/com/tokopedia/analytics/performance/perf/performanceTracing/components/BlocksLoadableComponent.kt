package com.tokopedia.analytics.performance.perf.performanceTracing.components

import com.tokopedia.analytics.performance.perf.performanceTracing.AppPerformanceTrace

class BlocksLoadableComponent(
    val isFinishedLoading: () -> Boolean = { false },
    val customBlocksName: String? = null
) : LoadableComponent {
    var loading: Boolean? = null

    init {
        recordComponent()
    }

    override fun isLoading(): Boolean {
        return loading ?: !isFinishedLoading.invoke()
    }

    override fun finishLoading() {
        loading = false
        AppPerformanceTrace.submitPerf(this)
    }

    override fun name(): String {
        return customBlocksName + "-${hashCode()}"
    }

    private fun recordComponent() {
        AppPerformanceTrace.submitPerf(this)
    }
}
