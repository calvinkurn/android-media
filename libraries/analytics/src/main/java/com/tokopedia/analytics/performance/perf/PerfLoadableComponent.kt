package com.tokopedia.analytics.performance.perf

class PerfLoadableComponent: LoadableComponent {
    var loading = true

    override fun isLoading(): Boolean {
        return loading
    }

    override fun finishLoading() {
        loading = false
    }
}
