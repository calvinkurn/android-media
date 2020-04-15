package com.tokopedia.analytics.performance.util

class PltPerformanceData(
        var startPageDuration: Long = System.currentTimeMillis(),
        var networkRequestDuration: Long = 0,
        var renderPageDuration: Long = 0,
        var overallDuration: Long = 0,
        var isCache: Boolean = false) {
}
