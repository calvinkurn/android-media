package com.tokopedia.shop.open.shop_open_revamp.analytic

import android.content.Context
import com.tokopedia.analytic_constant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.HashMap


class ShopOpenRevampTracking (context: Context) {

    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }
    private var trackingQueue = TrackingQueue(context)

    private val EVENT = "event"
    private val EVENT_CATEGORY = "eventCategory"
    private val EVENT_ACTION = "eventAction"
    private val EVENT_LABEL = "eventLabel"

    private val EVENT_VALUE = "clickCreateShop"
    private val EVENT_CATEGORY_VALUE = "registration page"

    fun clickCreateShop(isSuccess: Boolean, shopDomainName: String) {
        val status = if (isSuccess) "succes" else "failed"
        val eventLabelValue = "$status $shopDomainName"
        val data = DataLayer.mapOf(
                EVENT, EVENT_VALUE,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - shop",
                EVENT_ACTION, "click register shop",
                EVENT_LABEL, eventLabelValue
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun clickShopDomainSuggestion(shopDomain: String) {
        val data = DataLayer.mapOf(
                EVENT, EVENT_VALUE,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - shop",
                EVENT_ACTION, "click shop domain recommendation",
                EVENT_LABEL, shopDomain
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun clickBackButtonFromInputShopPage() {
        val data = DataLayer.mapOf(
                EVENT, EVENT_VALUE,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - shop",
                EVENT_ACTION, "click back",
                EVENT_LABEL, ""
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun clickTextTermsAndConditions() {
        val data = DataLayer.mapOf(
                EVENT, EVENT_VALUE,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - shop",
                EVENT_ACTION, "click term and condition",
                EVENT_LABEL, ""
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun clickTextPrivacyPolicy() {
        val data = DataLayer.mapOf(
                EVENT, EVENT_VALUE,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - shop",
                EVENT_ACTION, "click privacy term",
                EVENT_LABEL, ""
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun clickBackButtonFromSurveyPage() {
        val data = DataLayer.mapOf(
                EVENT, EVENT_VALUE,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - survey",
                EVENT_ACTION, "click back",
                EVENT_LABEL, ""
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun clickButtonNextFromSurveyPage() {
        val data = DataLayer.mapOf(
                EVENT, EVENT_VALUE,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - survey",
                EVENT_ACTION, "click continue",
                EVENT_LABEL, ""
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun clickTextSkipFromSurveyPage() {
        val data = DataLayer.mapOf(
                EVENT, EVENT_VALUE,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - survey",
                EVENT_ACTION, "click skip",
                EVENT_LABEL, ""
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }
}