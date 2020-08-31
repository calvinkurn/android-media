package com.tokopedia.officialstore.environment

import com.tokopedia.analytics.performance.util.PltPerformanceData

interface EspressoPerformanceActivity {
    fun getPltPerformanceResultData(): PltPerformanceData?
}