package com.tokopedia.tkpd.flashsale.util.tracker

import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.EVENT
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.Key.TRACKER_ID
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CampaignDetailPageTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickSeeCriteriaEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click lihat kriteria")
            .setEventCategory("flash sale - campaign detail")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37211")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickRegisterEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click daftar")
            .setEventCategory("flash sale - campaign detail")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37212")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickCheckReasonEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click cek alasan")
            .setEventCategory("flash sale - campaign detail")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37213")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickBulkChooseEvent(campaignId: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click pilih sekaligus")
            .setEventCategory("flash sale - product list")
            .setEventLabel(campaignId)
            .setCustomProperty(TRACKER_ID, "37244")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickAddProductEvent(campaignId: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click tambah produk")
            .setEventCategory("flash sale - product list")
            .setEventLabel(campaignId)
            .setCustomProperty(TRACKER_ID, "37249")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickDeleteEvent(campaignId: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click hapus")
            .setEventCategory("flash sale - product list")
            .setEventLabel(campaignId)
            .setCustomProperty(TRACKER_ID, "37250")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickEditEvent(campaignId: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click ubah")
            .setEventCategory("flash sale - product list")
            .setEventLabel(campaignId)
            .setCustomProperty(TRACKER_ID, "37251")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
