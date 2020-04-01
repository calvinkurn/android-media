package com.tokopedia.home

import com.tokopedia.analytics.performance.util.PerformanceData

interface EspressoPerformanceActivity {
    fun getPerformanceResultData() : PerformanceData?
}