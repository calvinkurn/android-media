package com.tokopedia.bmsm_widget.util.tracker

import com.tokopedia.bmsm_widget.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GiftListBottomSheetTracker @Inject constructor(private val userSession: UserSessionInterface) {

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4448
    // Tracker ID: 49755
    fun sendImpressionOpenGiftListBottomSheet(offerId: Long, warehouseId: Long) {
        val eventLabel = "$offerId - $warehouseId"

        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_VIEW_PG_IRIS)
            .setEventAction("impression hadiah")
            .setEventCategory("olp bmgm - minicart")
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "49755")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_PASCAL_CASE)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4448
    // Tracker ID: 49756
    fun sendClickTapChip(offerId: Long, warehouseId: Long, tierId: Long, chipName: String) {
        val eventLabel = "$offerId - $warehouseId - $tierId - $chipName"

        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click tab tier hadiah")
            .setEventCategory("olp bmgm - minicart")
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "49756")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_PASCAL_CASE)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4448
    // Tracker ID: 49757
    fun sendClickCloseGiftListBottomSheet(offerId: Long, warehouseId: Long) {
        val eventLabel = "$offerId - $warehouseId"

        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click close hadiah")
            .setEventCategory("olp bmgm - minicart")
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "49757")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_PASCAL_CASE)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }
}
