package com.tokopedia.shop.open.analytic

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics

class ShopOpenRevampTracking() {

    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }

    private val EVENT_VALUE = "clickCreateShop"
    private val EVENT_CATEGORY_VALUE = "registration page"

    fun sendScreenNameTracker(screenName: String) {
        tracker.sendScreenAuthenticated(screenName)
    }

    fun clickCreateShop() {
        tracker.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_VALUE,
                "$EVENT_CATEGORY_VALUE - shop",
                "click register shop",
                ""
            )
        )
    }

    fun clickShopDomainSuggestion(shopDomain: String) {
        tracker.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_VALUE,
                "$EVENT_CATEGORY_VALUE - shop",
                "click shop domain recommendation",
                shopDomain
            )
        )
    }

    fun clickBackButtonFromInputShopPage() {
        tracker.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_VALUE,
                "$EVENT_CATEGORY_VALUE - shop",
                "click back",
                ""
            )
        )
    }

    fun clickTextTermsAndConditions() {
        tracker.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_VALUE,
                "$EVENT_CATEGORY_VALUE - shop",
                "click term and condition",
                ""
            )
        )
    }

    fun clickTextPrivacyPolicy() {
        tracker.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_VALUE,
                "$EVENT_CATEGORY_VALUE - shop",
                "click privacy term",
                ""
            )
        )
    }

    fun clickBackButtonFromSurveyPage() {
        tracker.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_VALUE,
                "$EVENT_CATEGORY_VALUE - survey",
                "click back",
                ""
            )
        )
    }

    fun clickButtonNextFromSurveyPage() {
        tracker.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_VALUE,
                "$EVENT_CATEGORY_VALUE - survey",
                "click continue",
                ""
            )
        )
    }

    fun clickTextSkipFromSurveyPage() {
        tracker.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_VALUE,
                "$EVENT_CATEGORY_VALUE - survey",
                "click skip",
                ""
            )
        )
    }
}
