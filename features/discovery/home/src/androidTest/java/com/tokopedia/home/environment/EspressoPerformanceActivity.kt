package com.tokopedia.home.environment

import com.tokopedia.analytics.performance.fpi.FpiPerformanceData
import com.tokopedia.analytics.performance.util.PltPerformanceData

interface EspressoPerformanceActivity {
    fun getFpiPerformanceResultData() : FpiPerformanceData?
    fun getPltPerformanceResultData(): PltPerformanceData?
}