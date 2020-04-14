package com.tokopedia.home.environment

import com.tokopedia.analytics.performance.util.FpiPerformanceData
import com.tokopedia.analytics.performance.util.PltPerformanceData

interface EspressoPerformanceActivity {
    fun getFpiPerformanceResultData() : FpiPerformanceData?
    fun getPltPerformanceResultData(): PltPerformanceData?
}