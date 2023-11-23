package com.tokopedia.shop.common.data.model

import com.tokopedia.shop.analytic.ShopPageTrackingConstant
import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShopInfoPageTracker @Inject constructor(private val userSession: UserSessionInterface) {

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4388
    // Tracker ID: 48699
    fun sendShopInfoOpenScreenImpression(shopId: String) {
        val isLoggedIn = if (userSession.isLoggedIn) "true" else "false"

        Tracker.Builder()
            .setEvent(ShopPageTrackingConstant.Event.OPEN_SCREEN)
            .setCustomProperty(
                ShopPageTrackingConstant.TRACKER_ID,
                ShopPageTrackingConstant.TrackerId.TRACKER_ID_OPEN_SHOP_INFO_SCREEN_IMPRESSION
            )
            .setBusinessUnit(ShopPageTrackingConstant.PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE)
            .setCustomProperty(ShopPageTrackingConstant.IS_LOGGED_IN_STATUS, isLoggedIn)
            .setCustomProperty(
                ShopPageTrackingConstant.PAGE_TYPE,
                ShopPageTrackingConstant.PageType.SHOP_INFO
            )
            .setCustomProperty(
                ShopPageTrackingConstant.SCREEN_NAME,
                ShopPageTrackingConstant.PageType.SHOP_INFO
            )
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4388
    // Tracker ID: 48700
    fun sendCtaExpandPharmacyInformationEvent(shopId: String) {
        Tracker.Builder()
            .setEvent(ShopPageTrackingConstant.CLICK_PG)
            .setEventAction(ShopPageTrackingConstant.EventAction.CLICK_CTA_EXPAND_PHARMACY_INFORMATION)
            .setEventCategory(ShopPageTrackingConstant.SHOP_PAGE_BUYER)
            .setEventLabel("")
            .setCustomProperty(
                ShopPageTrackingConstant.TRACKER_ID,
                ShopPageTrackingConstant.TrackerId.TRACKER_ID_CTA_EXPAND_PHARMACY_INFO_CLICK
            )
            .setBusinessUnit(ShopPageTrackingConstant.PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4388
    // Tracker ID: 48701
    fun sendCtaViewPharmacyLocationEvent(shopId: String) {
        Tracker.Builder()
            .setEvent(ShopPageTrackingConstant.CLICK_PG)
            .setEventAction(ShopPageTrackingConstant.EventAction.CLICK_CTA_PHARMACY_LOCATION)
            .setEventCategory(ShopPageTrackingConstant.SHOP_PAGE_BUYER)
            .setEventLabel("")
            .setCustomProperty(
                ShopPageTrackingConstant.TRACKER_ID,
                ShopPageTrackingConstant.TrackerId.TRACKER_ID_CTA_VIEW_PHARMACY_LOCATION_CLICK
            )
            .setBusinessUnit(ShopPageTrackingConstant.PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4388
    // Tracker ID: 48702
    fun sendTapShopRatingEvent(shopId: String) {
        Tracker.Builder()
            .setEvent(ShopPageTrackingConstant.CLICK_PG)
            .setEventAction(ShopPageTrackingConstant.EventAction.CLICK_ICON_SHOP_REVIEW)
            .setEventCategory(ShopPageTrackingConstant.SHOP_PAGE_BUYER)
            .setEventLabel("")
            .setCustomProperty(
                ShopPageTrackingConstant.TRACKER_ID,
                ShopPageTrackingConstant.TrackerId.TRACKER_ID_ICON_VIEW_ALL_SHOP_REVIEW_CLICK
            )
            .setBusinessUnit(ShopPageTrackingConstant.PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4388
    // Tracker ID: 48703
    fun sendReviewImpression(reviewIndex: Int, shopId: String) {
        Tracker.Builder()
            .setEvent(ShopPageTrackingConstant.Event.VIEW_PG_IRIS)
            .setEventAction(ShopPageTrackingConstant.EventAction.IMPRESSION_SHOP_REVIEW)
            .setEventCategory(ShopPageTrackingConstant.SHOP_PAGE_BUYER)
            .setEventLabel(reviewIndex.toString())
            .setCustomProperty(
                ShopPageTrackingConstant.TRACKER_ID,
                ShopPageTrackingConstant.TrackerId.TRACKER_ID_SHOP_REVIEW_IMPRESSION
            )
            .setBusinessUnit(ShopPageTrackingConstant.PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4388
    // Tracker ID: 48704
    fun sendClickShopNoteEvent(shopNote: ShopNote, shopId: String) {
        val shopNoteId = shopNote.id
        val templateId = 0
        val eventLabel = "$shopNoteId - $templateId"

        Tracker.Builder()
            .setEvent(ShopPageTrackingConstant.CLICK_PG)
            .setEventAction(ShopPageTrackingConstant.EventAction.CLICK_SHOP_NOTE)
            .setEventCategory(ShopPageTrackingConstant.SHOP_PAGE_BUYER)
            .setEventLabel(eventLabel)
            .setCustomProperty(
                ShopPageTrackingConstant.TRACKER_ID,
                ShopPageTrackingConstant.TrackerId.TRACKER_ID_SHOP_NOTE_CLICK
            )
            .setBusinessUnit(ShopPageTrackingConstant.PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }
}
