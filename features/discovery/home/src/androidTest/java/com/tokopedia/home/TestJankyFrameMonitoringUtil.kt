package com.tokopedia.home

import com.tokopedia.analytics.performance.util.JankyFrameMonitoringUtil

/**
 * @author by devarafikry on 2020-03-03
 */
class TestJankyFrameMonitoringUtil: JankyFrameMonitoringUtil() {
    override fun getPageTag(pageName: String, subPageName: String): String {
        val testPageName = "test_performance_$pageName"
        return super.getPageTag(testPageName, subPageName)
    }
}