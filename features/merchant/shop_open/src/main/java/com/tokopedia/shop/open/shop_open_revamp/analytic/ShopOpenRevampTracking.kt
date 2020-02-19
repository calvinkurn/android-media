package com.tokopedia.shop.open.shop_open_revamp.analytic

import android.content.Context
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.HashMap


class ShopOpenRevampTracking (context: Context) {

    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }

    private val EVENT_VALUE = "clickCreateShop"
    private val EVENT_CATEGORY_VALUE = "registration page"

    fun sendScreenHooray() {
        val screenName = "/registration page - shop/hooray"
        tracker.sendScreenAuthenticated(screenName)
    }

    fun sendScreenCongratulations() {
        val screenName = "/registration page - shop/congratulation"
        tracker.sendScreenAuthenticated(screenName)
    }

    fun clickCreateShop(isSuccess: Boolean, shopDomainName: String) {
        val status = if (isSuccess) "succes" else "failed"
        val eventLabelValue = "$status $shopDomainName"
        tracker.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        EVENT_VALUE,
                        "$EVENT_CATEGORY_VALUE - shop",
                        "click register shop",
                        eventLabelValue
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