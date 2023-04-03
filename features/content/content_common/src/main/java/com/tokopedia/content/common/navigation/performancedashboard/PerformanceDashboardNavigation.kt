package com.tokopedia.content.common.navigation.performancedashboard

object PerformanceDashboardNavigation {

    private const val PERFORMANCE_DASHBOARD_URL_PROD = "https://tokopedia.com/play/live"
    private const val PERFORMANCE_DASHBOARD_URL_STAGING = "https://staging.tokopedia.com/play/live"
    private const val PERFORMANCE_DASHBOARD_URL_WEB_VIEW = "tokopedia://webview?pull_to_refresh=true&url=%s"

    fun getPerformanceDashboardAppLink(): String {
        return String.format(PERFORMANCE_DASHBOARD_URL_WEB_VIEW, PERFORMANCE_DASHBOARD_URL_STAGING)
    }

}
