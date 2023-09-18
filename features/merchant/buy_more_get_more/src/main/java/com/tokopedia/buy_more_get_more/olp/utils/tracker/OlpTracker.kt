package com.tokopedia.buy_more_get_more.olp.utils.tracker

import android.text.TextUtils
import com.tokopedia.buy_more_get_more.olp.utils.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class OlpTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendOpenScreenEvent(shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_OPEN_SCREEN)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46752")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setCustomProperty(TrackerConstant.LOGIN_STATUS, userSession.isLoggedIn)
            .setCustomProperty(TrackerConstant.SCREEN_NAME, "OfferLandingPage")
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickBackButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click back button")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46753")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickShareButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click share button")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46754")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickShopCtaButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click shop cta")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46755")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickKeranjangButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click keranjang")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46756")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickBurgerButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click burger")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46757")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickSnkButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click snk")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46758")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendImpressSnkEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_VIEW_PG_IRIS)
            .setEventAction("impression snk bottomsheet")
            .setEventCategory("${TrackerConstant.EVENT_CATEGORY} - snk")
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46759")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickCloseSnkButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click close snk")
            .setEventCategory("${TrackerConstant.EVENT_CATEGORY} - snk")
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46760")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickFilterDropdownButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click filter dropdown")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46761")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickFilterButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click filter")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46762")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendImpressProductCardEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_VIEW_ITEM_LIST)
            .setEventAction("impression product card")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46768")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickProductCardEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_SELECT_CONTENT)
            .setEventAction("click product card")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46769")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickAtcEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_ADD_TO_CART)
            .setEventAction("click atc")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46775")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendImpressVariantEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_VIEW_PG_IRIS)
            .setEventAction("impression variant")
            .setEventCategory("${TrackerConstant.EVENT_CATEGORY} - variant")
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46776")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickCloseVariantEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click close variant")
            .setEventCategory("${TrackerConstant.EVENT_CATEGORY} - variant")
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46777")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickChipsVariantEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click variant chip")
            .setEventCategory("${TrackerConstant.EVENT_CATEGORY} - variant")
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46778")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    private fun joinDash(vararg s: String?): String {
        return TextUtils.join(" - ", s)
    }
}
