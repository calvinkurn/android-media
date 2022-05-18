package com.tokopedia.gopayhomewidget.analytics

sealed interface AnalyticsEventGenerator {

    data class WidgetImpressionAnalytics(val widgetType: String) : AnalyticsEventGenerator

    data class WidgetCtaClickedButton(
        val widgetType: String,
        val redirectionPage: String
    ) : AnalyticsEventGenerator
}