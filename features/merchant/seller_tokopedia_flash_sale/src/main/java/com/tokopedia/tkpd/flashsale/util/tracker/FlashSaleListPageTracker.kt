package com.tokopedia.tkpd.flashsale.util.tracker

import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class FlashSaleListPageTracker @Inject constructor(private val userSession: UserSessionInterface) {

    //region All tab
    fun sendClickReadArticleEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click artikel")
            .setEventCategory("flash sale - campaign list")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37205")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
    //endregion

    //region Upcoming tab
    fun sendRegisterFlashSaleEvent(flashSaleId: Long, tabName: String) {
        val eventLabel = "$flashSaleId - ${tabName.formatTabStatusName()}"
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click daftar")
            .setEventCategory("flash sale - campaign list")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37204")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
    //endregion

    //region Registered tab
    fun sendAddProductButtonClickEvent(flashSaleId: Long, tabName: String) {
        val eventLabel = "$flashSaleId - ${tabName.formatTabStatusName()}"

        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click tambah produk")
            .setEventCategory("flash sale - campaign list")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37206")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendUpdateProductButtonClickEvent(flashSaleId: Long, tabName: String) {
        val eventLabel = "$flashSaleId - ${tabName.formatTabStatusName()}"

        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click ubah produk")
            .setEventCategory("flash sale - campaign list")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37207")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendViewCampaignDetailButtonClickEvent(flashSaleId: Long, tabName: String) {
        val eventLabel = "$flashSaleId - ${tabName.formatTabStatusName()}"

        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click lihat detail")
            .setEventCategory("flash sale - campaign list")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37208")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
    //endregion

    //region Ongoing tab
    fun sendOngoingFlashSaleCardClickEvent(flashSaleId: Long, tabName: String) {
        val eventLabel = "$flashSaleId - ${tabName.formatTabStatusName()}"

        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click campaign card")
            .setEventCategory("flash sale - campaign list")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37209")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
    //endregion

    private fun String.formatTabStatusName() : String {
        return when(this) {
            "upcoming" -> "Akan Datang"
            "registered" -> "Terdaftar"
            "ongoing" -> "Berlangsung"
            "finished" -> "Selesai"
            else -> this
        }
    }
}