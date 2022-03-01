package com.tokopedia.gopayhomewidget.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AnalyticsUpload @Inject constructor(
        private val userSession: dagger.Lazy<UserSessionInterface>,
) {

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    fun sendWidgetAnalyticsEvent(analyticsEventGenerator: AnalyticsEventGenerator) {
        when (analyticsEventGenerator) {
            is AnalyticsEventGenerator.WidgetCtaClickedButton -> sendClickEvent(analyticsEventGenerator.redirectionPage, analyticsEventGenerator.widgetType)
            is AnalyticsEventGenerator.WidgetImpressionAnalytics -> sendImpressionEvent(analyticsEventGenerator.widgetType)
        }
    }

    private fun sendImpressionEvent(widgetType: String) {
        val map = TrackAppUtils.gtmData(
                WIDGET_IMPRESSION_EVENT,
                WIDGET_IMPRESSION_CATEGORY,
                WIDGET_IMPRESSION_ACTION,
                "${userSession.get().name} - $widgetType - $PARTNER_NAME"
        )
        sendGeneralEvent(map)
    }

    private fun sendClickEvent(redirectionPage: String, widgetType: String) {
        val map = TrackAppUtils.gtmData(
                WIDGET_CLICK_EVENT,
                WIDGET_IMPRESSION_CATEGORY,
                WIDGET_CLICK_ACTION,
                "${userSession.get().name} - $widgetType - $PARTNER_NAME - $redirectionPage"
        )
        sendGeneralEvent(map)
    }

    private fun sendGeneralEvent(map: MutableMap<String, Any>) {
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_FINTECH
        map[KEY_CURRENT_SITE] = CURRENT_SITE_FINTECH
        map[KEY_USER_ID] = userSession.get().userId
        analyticTracker.sendGeneralEvent(map)
    }

    companion object {
        const val PARTNER_NAME = "Go-Cicil"
        const val KEY_USER_ID = "userId"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"
        const val BUSINESS_UNIT_FINTECH = "fintechPaylater"
        const val CURRENT_SITE_FINTECH = "TokopediaFintech"
        const val WIDGET_IMPRESSION_EVENT = "viewFintechIris"
        const val WIDGET_CLICK_EVENT = "clickFintech"
        const val WIDGET_IMPRESSION_ACTION = "to do widget - impression"
        const val WIDGET_CLICK_ACTION = "to do widget - click"
        const val WIDGET_IMPRESSION_CATEGORY = "fin - bnpl home"
    }
}