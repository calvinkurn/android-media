package com.tokopedia.analytics.performance.perf

data class BlocksPerformanceModel(
    val inflateTime: Long = 0L,
    val capturedBlocks: Set<String> = setOf()
)
