package com.tokopedia.analytics.performance.perf

class BlocksLoadableComponent(
    val isFinishedLoading: () -> Boolean = { false },
    val customBlocksName: String? = null
): LoadableComponent {
    var loading: Boolean? = null

    override fun isLoading(): Boolean {
        return loading?: !isFinishedLoading.invoke()
    }

    override fun finishLoading() {
        loading = false
    }

    override fun name(): String {
        return customBlocksName+"-${hashCode()}"
    }
}
