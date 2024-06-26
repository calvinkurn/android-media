package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.TrackerConstant.TRACKER_ID
import com.tokopedia.mvc.util.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.mvc.util.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.mvc.util.constant.TrackerConstant.EVENT
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SummaryPageTracker @Inject constructor(private val userSession: UserSessionInterface) {

    companion object {
        private const val EC_CREATION_SUCCESS = "kupon toko saya - creation success"
        private const val EC_CREATION_PROCESS = "kupon toko saya - creation"
        private const val EC_CREATION_SUMMARY = "kupon toko saya - summary"
        private const val EL_VOUCHER_ID_PREFIX = "voucher id: "
        private const val EL_CAMPAIGN_ID_PREFIX = "campaign id: "
        private const val EL_DELIMITER = " - "
        private const val EL_TOPADS = "iklan toko"
        private const val EL_BROADCAST = "broadcast"
    }

    // section - pup up success

    fun sendClickBagikanKuponPopUpEvent(shareMethodName: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click bagikan kupon - pop up")
            .setEventCategory(EC_CREATION_SUCCESS)
            .setEventLabel(shareMethodName)
            .setCustomProperty(TRACKER_ID, "39425")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickBroadcastPopUpEvent() {
        sendClickBagikanKuponPopUpEvent(EL_BROADCAST)
    }

    fun sendClickTopadsPopUpEvent() {
        sendClickBagikanKuponPopUpEvent(EL_TOPADS)
    }

    fun sendClickCloseEvent(voucherId: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click close")
            .setEventCategory(EC_CREATION_SUCCESS)
            .setEventLabel(EL_VOUCHER_ID_PREFIX + voucherId)
            .setCustomProperty(TRACKER_ID, "39427")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    // section - pup up success

    // section - add coupon

    fun sendClickCheckboxSyaratKetentuanEvent() {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click checkbox syarat ketentuan")
            .setEventCategory(EC_CREATION_PROCESS)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, "39422")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickBuatKuponEvent() {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click buat kupon")
            .setEventCategory(EC_CREATION_PROCESS)
            .setEventLabel(getEventLavel("", ""))
            .setCustomProperty(TRACKER_ID, "39423")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickKembaliArrowFifthStepEvent() {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click kembali arrow - fifth step")
            .setEventCategory(EC_CREATION_PROCESS)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, "39424")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    // section - add coupon

    // section - edit coupon

    fun sendClickUbahEvent(voucherId: String, sectionName: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click ubah")
            .setEventCategory(EC_CREATION_SUMMARY)
            .setEventLabel(getEventLavel(voucherId, "") + EL_DELIMITER + sectionName)
            .setCustomProperty(TRACKER_ID, "40622")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickCheckboxOnTncEvent(voucherId: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click checkbox on tnc")
            .setEventCategory(EC_CREATION_SUMMARY)
            .setEventLabel(getEventLavel(voucherId, ""))
            .setCustomProperty(TRACKER_ID, "40623")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickSimpanEvent(voucherId: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click simpan")
            .setEventCategory(EC_CREATION_SUMMARY)
            .setEventLabel(getEventLavel(voucherId, ""))
            .setCustomProperty(TRACKER_ID, "40624")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
    
    fun sendClickArrowBackEvent(voucherId: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click arrow back")
            .setEventCategory(EC_CREATION_SUMMARY)
            .setEventLabel(getEventLavel(voucherId, ""))
            .setCustomProperty(TRACKER_ID, "40625")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    // section - edit coupon

    private fun getEventLavel(voucherId: String, campaignId: String): String {
        return EL_VOUCHER_ID_PREFIX + voucherId + EL_DELIMITER +
            EL_CAMPAIGN_ID_PREFIX + campaignId
    }
}
