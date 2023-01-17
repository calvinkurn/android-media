package com.tokopedia.gopayhomewidget.analytics

sealed interface AnalyticsEventGenerator {

    data class WidgetImpressionAnalytics(
        val caseType: String,
        val partnerName: String
    ) : AnalyticsEventGenerator

    data class WidgetCtaClickedButton(
        val caseType: String,
        val redirectionPage: String,
        val partnerName: String
    ) : AnalyticsEventGenerator
}
