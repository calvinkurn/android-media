package com.tokopedia.analytics.performance.perf.performanceTracing.strategy

data class ViewInfo(
    val name: String = "",
    val resourceIdString: String = "",
    val isVisible: Boolean = false,
    val height: Int = 0,
    val location: IntArray = intArrayOf(),
    var directChilds: List<ViewInfo> = listOf()
)
