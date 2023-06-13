package com.tokopedia.analytics.performance.perf

data class ViewPerformanceModel(
    val id: Int = 0,
    val idName: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val name: String = "",
    val inflateTime: Long = 0L,
    val isViewGroup: Boolean = false
)
