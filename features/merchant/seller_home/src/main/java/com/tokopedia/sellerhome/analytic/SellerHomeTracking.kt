package com.tokopedia.sellerhome.analytic

import com.tokopedia.track.TrackApp

/**
 * Created By @ilhamsuaib on 2020-02-11
 */

object SellerHomeTracking {

    fun sendImpressionCardEvent(datKey: String, state: String, cardValue: String) {
        val map = createMap(
                TrackingConstant.VIEW_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_CARD, "$datKey $state").joinToString(" - "),
                cardValue
        )
        sendGeneralEvent(map)
    }

    fun sendClickCardEvent(datKey: String, state: String, cardValue: String) {
        val map = createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_CARD, "$datKey $state").joinToString(" - "),
                cardValue
        )
        sendGeneralEvent(map)
    }

    private fun createMap(event: String, category: String, action: String, label: String): MutableMap<String, Any> {
        return mutableMapOf(
                TrackingConstant.EVENT to event,
                TrackingConstant.EVENT_CATEGORY to category,
                TrackingConstant.EVENT_ACTION to action,
                TrackingConstant.EVENT_LABEL to label
        )
    }

    private fun sendGeneralEvent(eventMap: MutableMap<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    private fun sendEnhanceEcommerceEvent(eventMap: MutableMap<String, Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }
}