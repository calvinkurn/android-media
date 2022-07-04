package com.tokopedia.shop.flashsale.common.tracker

import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShopFlashSaleTracker @Inject constructor(private val userSession : UserSessionInterface) {

    companion object {
        private const val BUSINESS_UNIT = "physical goods"
        private const val CURRENT_SITE = "tokopediaseller"
        private const val EVENT = "clickPG"
    }

    //region Campaign List Page Tracker
    fun sendClickCreateCampaignEvent() {
        Tracker.Builder()
            .setBusinessUnit(BUSINESS_UNIT)
            .setEventCategory("fs toko - campaign aktif")
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .setEvent(EVENT)
            .setEventAction("click buat campaign")
            .setEventLabel("")
            .build()
            .send()
    }
    //endregion

    //region Campaign Info Page Tracker
    fun sendClickButtonProceedOnCampaignInfoPageEvent() {
        Tracker.Builder()
            .setBusinessUnit(BUSINESS_UNIT)
            .setEventCategory("fs toko - creation")
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .setEvent(EVENT)
            .setEventAction("click lanjut")
            .setEventLabel("")
            .build()
            .send()
    }
    //endregion

    //region Manage Highlighted Product Page Tracker
    fun sendClickButtonProceedOnManageHighlightPageEvent() {
        Tracker.Builder()
            .setBusinessUnit(BUSINESS_UNIT)
            .setEventCategory("fs toko - kelola produk highlight")
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .setEvent(EVENT)
            .setEventAction("click lanjut")
            .setEventLabel("")
            .build()
            .send()
    }
    //endregion

    //region Share Component
    fun sendClickShareEvent(selectedChannelName: String) {
        Tracker.Builder()
            .setBusinessUnit(BUSINESS_UNIT)
            .setEventCategory("fs toko - campaign aktif")
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .setEvent(EVENT)
            .setEventAction("click share")
            .setEventLabel(selectedChannelName)
            .build()
            .send()
    }
    //endregion
}