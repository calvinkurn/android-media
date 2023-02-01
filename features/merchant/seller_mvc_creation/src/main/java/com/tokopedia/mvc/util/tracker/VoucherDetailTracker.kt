package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.domain.entity.VoucherDetailData
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus.*
import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class VoucherDetailTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickBroadCastChatEvent(data: VoucherDetailData) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click broadcast chat")
            .setEventCategory(TrackerConstant.VoucherDetail.event)
            .setEventLabel(data.toEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40626")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClick3DotsButtonEvent(data: VoucherDetailData) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click 3 dots button")
            .setEventCategory(TrackerConstant.VoucherDetail.event)
            .setEventLabel(data.toEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40627")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickDownloadEvent(data: VoucherDetailData) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click download")
            .setEventCategory(TrackerConstant.VoucherDetail.event)
            .setEventLabel(data.toEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40628")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickEditEvent(data: VoucherDetailData) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click edit")
            .setEventCategory(TrackerConstant.VoucherDetail.event)
            .setEventLabel(data.toEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40629")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickShareEvent(data: VoucherDetailData) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click share")
            .setEventCategory(TrackerConstant.VoucherDetail.event)
            .setEventLabel(data.toEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40631")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickDuplicateEvent(data: VoucherDetailData) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click duplikat")
            .setEventCategory(TrackerConstant.VoucherDetail.event)
            .setEventLabel(data.toEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40635")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickArrowBackEvent(data: VoucherDetailData) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click arrow back")
            .setEventCategory(TrackerConstant.VoucherDetail.event)
            .setEventLabel(data.toEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40637")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickTNCEvent(data: VoucherDetailData) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click lihat syarat dan ketentuan penggunaan kupon")
            .setEventCategory(TrackerConstant.ThreeDotsMenu.event)
            .setEventLabel(data.toEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40638")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickBatalkanEvent(data: VoucherDetailData) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click batalkan")
            .setEventCategory(TrackerConstant.ThreeDotsMenu.event)
            .setEventLabel(data.toEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40639")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickHentikanEvent(data: VoucherDetailData) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click hentikan")
            .setEventCategory(TrackerConstant.ThreeDotsMenu.event)
            .setEventLabel(data.toEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40642")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    private fun VoucherDetailData.toEventLabel(): String {
        return "voucher id: ${this.voucherId} - voucher status: ${this.voucherStatus}"
    }

    private fun VoucherStatus.toEventLabel(): String {
        return when (this) {
            NOT_STARTED -> "mendatang"
            ONGOING -> "berlangsung"
            STOPPED -> "dihentikan"
            else -> "berakhir"
        }
    }
}
