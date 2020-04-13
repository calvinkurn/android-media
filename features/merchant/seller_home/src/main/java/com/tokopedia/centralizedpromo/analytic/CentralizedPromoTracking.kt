package com.tokopedia.centralizedpromo.analytic

import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_EDUCATION_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_EDUCATION_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_ON_GOING_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_ON_GOING_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_PROMO_CREATION_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_PROMO_CREATION_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CATEGORY_ADS_AND_PROMO
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_IMPRESSION
import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.track.TrackApp

// Doc : https://docs.google.com/spreadsheets/d/1d6OCqZyVOMsYrEChc-xwj1Ta_5G9bvCKIzOFw4kq4BA
object CentralizedPromoTracking {
    @Suppress("SameParameterValue")
    private fun createMap(event: String, category: String, action: String, label: String): MutableMap<String, Any> {
        return mutableMapOf(
                TrackingConstant.EVENT to event,
                TrackingConstant.EVENT_CATEGORY to category,
                TrackingConstant.EVENT_ACTION to action,
                TrackingConstant.EVENT_LABEL to label
        )
    }

    fun sendImpressionOnGoingPromoStatus(widgetName: String, value: Int, state: String) {
        val data = createMap(
                event = EVENT_NAME_IMPRESSION,
                category = EVENT_CATEGORY_ADS_AND_PROMO,
                action = arrayOf(EVENT_ACTION_ON_GOING_IMPRESSION, widgetName, state).joinToString(" - "),
                label = value.toString()
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickOnGoingPromoStatus(widgetName: String, value: Int, state: String) {
        val data = createMap(
                event = EVENT_NAME_CLICK,
                category = EVENT_CATEGORY_ADS_AND_PROMO,
                action = arrayOf(EVENT_ACTION_ON_GOING_CLICK, widgetName, state).joinToString(" - "),
                label = value.toString()
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickOnGoingPromoFooter(widgetName: String, footerText: String) {
        val data = createMap(
                event = EVENT_NAME_CLICK,
                category = EVENT_CATEGORY_ADS_AND_PROMO,
                action = arrayOf(EVENT_ACTION_ON_GOING_CLICK, widgetName, footerText).joinToString(" - "),
                label = ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendImpressionPromoCreation(widgetName: String) {
        val data = createMap(
                event = EVENT_NAME_IMPRESSION,
                category = EVENT_CATEGORY_ADS_AND_PROMO,
                action = arrayOf(EVENT_ACTION_PROMO_CREATION_IMPRESSION, widgetName).joinToString(" - "),
                label = ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickPromoCreation(widgetName: String) {
        val data = createMap(
                event = EVENT_NAME_CLICK,
                category = EVENT_CATEGORY_ADS_AND_PROMO,
                action = arrayOf(EVENT_ACTION_PROMO_CREATION_CLICK, widgetName).joinToString(" - "),
                label = ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendImpressionArticle() {
        val data = createMap(
                event = EVENT_NAME_IMPRESSION,
                category = EVENT_CATEGORY_ADS_AND_PROMO,
                action = EVENT_ACTION_EDUCATION_IMPRESSION,
                label = ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickArticleItem(title: String) {
        val data = createMap(
                event = EVENT_NAME_CLICK,
                category = EVENT_CATEGORY_ADS_AND_PROMO,
                action = arrayOf(EVENT_ACTION_EDUCATION_CLICK, title).joinToString(" - "),
                label = ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }
}