package com.tokopedia.homenav.view.activity

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface

interface HomeNavPerformanceInterface {
    fun getNavPerformanceInterface(): PageLoadTimePerformanceInterface
}