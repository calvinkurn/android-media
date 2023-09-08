package com.tokopedia.minicart.bmgm.analytics

import com.tokopedia.track.builder.Tracker

/**
 * Created by @ilhamsuaib on 06/09/23.
 */

object BmgmMiniCartTracker {

    private const val CLICK_EVENT = "clickPG"
    private const val IMPRESSION_EVENT = "viewPGIris"
    private const val BUSINESS_UNIT = "Physical Goods"
    private const val CURRENT_SITE = "tokopediamarketplace"

    fun sendImpressionMinicartEvent(
        offerId: String,
        warehouseId: String,
        shopId: String,
        userId: String
    ) {
        val eventLabel = "$offerId - $warehouseId"
        Tracker.Builder()
            .setEvent(IMPRESSION_EVENT)
            .setEventAction("impression minicart")
            .setEventCategory("olp bmgm - minicart")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46772")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setCustomProperty("sessionIris", "session_id")
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCloseMinicartEvent(
        offerId: String,
        warehouseId: String,
        shopId: String,
        userId: String
    ) {
        val eventLabel = "$offerId - $warehouseId"
        Tracker.Builder()
            .setEvent(CLICK_EVENT)
            .setEventAction("click close minicart")
            .setEventCategory("olp bmgm - minicart")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46773")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setCustomProperty("sessionIris", "session_id")
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCekKeranjangEvent(
        offerId: String,
        warehouseId: String,
        shopId: String,
        userId: String
    ) {
        val eventLabel = "$offerId - $warehouseId"
        Tracker.Builder()
            .setEvent(CLICK_EVENT)
            .setEventAction("click cek keranjang")
            .setEventCategory("olp bmgm")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46774")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setCustomProperty("sessionIris", "session_id")
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }
}