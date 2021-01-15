package com.tokopedia.analytics.performance.util

class PltPerformanceData(
        var startPageDuration: Long = System.currentTimeMillis(),
        var networkRequestDuration: Long = 0,
        var renderPageDuration: Long = 0,
        var overallDuration: Long = 0,
        var isSuccess: Boolean = true,
        var isCache: Boolean = false,
        var attribution: HashMap<String, String> = hashMapOf(),
        var customMetric: HashMap<String, Long> = hashMapOf()
)
