package com.tokopedia.mvc.util.tracker

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.util.constant.MvcTrackerConstant
import com.tokopedia.mvc.util.constant.MvcTrackerConstant.MVC_BUSINESS_UNIT
import com.tokopedia.mvc.util.constant.MvcTrackerConstant.MVC_CURRENT_SITE
import com.tokopedia.mvc.util.constant.MvcTrackerConstant.MVC_EVENT
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.mvc.R
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus

class VoucherListActionTracker @Inject constructor(
    private val userSession: UserSessionInterface,
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val EC_VOUCHER_LIST_ACTION_PAGE = "kupon toko saya - daftar kupon"
        private const val VOUCHER_ID_LABEL_PREFIX = "voucher id: "
        private const val VOUCHER_STATUS_LABEL_PREFIX = "voucher status: "
        private const val EL_DELIMITER = " - "
    }
    
    fun sendClickUbahQuotaEvent(voucher: Voucher) {
        Tracker.Builder()
            .setEvent(MVC_EVENT)
            .setEventAction("click ubah quota")
            .setEventCategory(EC_VOUCHER_LIST_ACTION_PAGE)
            .setEventLabel(getEventLabel(voucher))
            .setCustomProperty(MvcTrackerConstant.TRACKER_ID, "39435")
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickUbahPeriodEvent(voucher: Voucher) {
        Tracker.Builder()
            .setEvent(MVC_EVENT)
            .setEventAction("click ubah period")
            .setEventCategory(EC_VOUCHER_LIST_ACTION_PAGE)
            .setEventLabel(getEventLabel(voucher))
            .setCustomProperty(MvcTrackerConstant.TRACKER_ID, "39436")
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickUbahEvent(voucher: Voucher) {
        Tracker.Builder()
            .setEvent(MVC_EVENT)
            .setEventAction("click ubah")
            .setEventCategory(EC_VOUCHER_LIST_ACTION_PAGE)
            .setEventLabel(getEventLabel(voucher))
            .setCustomProperty(MvcTrackerConstant.TRACKER_ID, "39437")
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickLihatDetailEvent(voucher: Voucher) {
        Tracker.Builder()
            .setEvent(MVC_EVENT)
            .setEventAction("click lihat detail")
            .setEventCategory(EC_VOUCHER_LIST_ACTION_PAGE)
            .setEventLabel(getEventLabel(voucher))
            .setCustomProperty(MvcTrackerConstant.TRACKER_ID, "39438")
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
    
    fun sendClickBroadcastChatEvent(voucher: Voucher) {
        Tracker.Builder()
            .setEvent(MVC_EVENT)
            .setEventAction("click broadcast chat")
            .setEventCategory(EC_VOUCHER_LIST_ACTION_PAGE)
            .setEventLabel(getEventLabel(voucher))
            .setCustomProperty(MvcTrackerConstant.TRACKER_ID, "39439")
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
    
    fun sendClickDownloadEvent(voucher: Voucher) {
        Tracker.Builder()
            .setEvent(MVC_EVENT)
            .setEventAction("click download")
            .setEventCategory(EC_VOUCHER_LIST_ACTION_PAGE)
            .setEventLabel(getEventLabel(voucher))
            .setCustomProperty(MvcTrackerConstant.TRACKER_ID, "39440")
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
    
    fun sendClickBatalkanEvent(voucher: Voucher) {
        Tracker.Builder()
            .setEvent(MVC_EVENT)
            .setEventAction("click batalkan")
            .setEventCategory(EC_VOUCHER_LIST_ACTION_PAGE)
            .setEventLabel(getEventLabel(voucher))
            .setCustomProperty(MvcTrackerConstant.TRACKER_ID, "39441")
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
    
    fun sendClickBagikanEvent(voucher: Voucher) {
        Tracker.Builder()
            .setEvent(MVC_EVENT)
            .setEventAction("click bagikan")
            .setEventCategory(EC_VOUCHER_LIST_ACTION_PAGE)
            .setEventLabel(getEventLabel(voucher))
            .setCustomProperty(MvcTrackerConstant.TRACKER_ID, "39442")
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
    
    fun sendClickHentikanEvent(voucher: Voucher) {
        Tracker.Builder()
            .setEvent(MVC_EVENT)
            .setEventAction("click hentikan")
            .setEventCategory(EC_VOUCHER_LIST_ACTION_PAGE)
            .setEventLabel(getEventLabel(voucher))
            .setCustomProperty(MvcTrackerConstant.TRACKER_ID, "39443")
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickDuplikatEvent(voucher: Voucher) {
        Tracker.Builder()
            .setEvent(MVC_EVENT)
            .setEventAction("click duplikat")
            .setEventCategory(EC_VOUCHER_LIST_ACTION_PAGE)
            .setEventLabel(getEventLabel(voucher))
            .setCustomProperty(MvcTrackerConstant.TRACKER_ID, "39444")
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    private fun getEventLabel(voucher: Voucher): String {
        val upcomingLabel = context.getString(R.string.smvc_status_upcoming_label)
        val ongoingLabel = context.getString(R.string.smvc_status_ongoing_label)
        val endedLabel = context.getString(R.string.smvc_status_ended_label)
        val stoppedLabel = context.getString(R.string.smvc_status_stopped_label)
        val statusLabel = when (voucher.status) {
            VoucherStatus.NOT_STARTED -> upcomingLabel
            VoucherStatus.ONGOING -> ongoingLabel
            VoucherStatus.ENDED -> endedLabel
            VoucherStatus.STOPPED -> stoppedLabel
            else -> ""
        }
        return VOUCHER_ID_LABEL_PREFIX + voucher.id + EL_DELIMITER +
            VOUCHER_STATUS_LABEL_PREFIX + statusLabel
    }

}
