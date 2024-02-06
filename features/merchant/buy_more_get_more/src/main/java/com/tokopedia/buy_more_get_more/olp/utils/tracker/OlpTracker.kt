package com.tokopedia.buy_more_get_more.olp.utils.tracker

import android.text.TextUtils
import androidx.core.os.bundleOf
import com.tokopedia.buy_more_get_more.olp.utils.constant.TrackerConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class OlpTracker @Inject constructor(private val userSession: UserSessionInterface) {

    companion object {
        private const val USER_SHARE_TYPE_GENERAL = "general"
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4201
    // Tracker ID: 46752
    fun sendOpenScreenEvent(shopId: String, offerId: String, warehouseId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_OPEN_SCREEN)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46752")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_OLP_BMGM)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_OLP_BMGM)
            .setCustomProperty(TrackerConstant.LOGIN_STATUS, userSession.isLoggedIn.toString())
            .setCustomProperty(TrackerConstant.SCREEN_NAME, "OfferLandingPage")
            .setShopId(shopId)
            .setEventLabel("$offerId - $warehouseId")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY_OLP_BMGM)
            .setEventAction(TrackerConstant.EVENT_OPEN_SCREEN)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4164
    // Tracker ID: 46753
    fun sendClickBackButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click back button")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY_OLP_BMGM)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46753")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_OLP_BMGM)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_OLP_BMGM)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4201
    // Tracker ID: 46864
    fun sendClickShareButtonEvent(offerTypeId: String, offerId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_COMMUNICATION)
            .setEventAction("click - share button")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY_BMSM_PAGE)
            .setEventLabel(joinDash(USER_SHARE_TYPE_GENERAL, offerTypeId, shopId, offerId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46864")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_SHARING_EXPERIENCE)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_OLP_BMGM)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4201
    // Tracker ID: 46865
    fun sendClickCloseShareButtonEvent(offerTypeId: String, offerId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_COMMUNICATION)
            .setEventAction("click - close share bottom sheet")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY_BMSM_PAGE)
            .setEventLabel(joinDash(USER_SHARE_TYPE_GENERAL, offerTypeId, shopId, offerId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46865")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_SHARING_EXPERIENCE)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_OLP_BMGM)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4201
    // Tracker ID: 46866
    fun sendClickSharingChannelEvent(offerTypeId: String, channel: String, offerId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_COMMUNICATION)
            .setEventAction("click - sharing channel")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY_BMSM_PAGE)
            .setEventLabel(joinDash(channel, USER_SHARE_TYPE_GENERAL, offerTypeId, shopId, offerId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46866")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_SHARING_EXPERIENCE)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_OLP_BMGM)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4201
    // Tracker ID: 46867
    fun sendViewSharingChannelEvent(offerTypeId: String, offerId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_VIEW_COMMUNICATION_IRIS)
            .setEventAction("view on sharing channel")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY_BMSM_PAGE)
            .setEventLabel(joinDash(USER_SHARE_TYPE_GENERAL, offerTypeId, shopId, offerId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46867")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_SHARING_EXPERIENCE)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_OLP_BMGM)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4164
    // Tracker ID: 46755
    fun sendClickShopCtaButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click shop cta")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY_OLP_BMGM)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46755")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_OLP_BMGM)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_OLP_BMGM)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4164
    // Tracker ID: 46756
    fun sendClickKeranjangButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click keranjang")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY_OLP_BMGM)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46756")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_OLP_BMGM)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_OLP_BMGM)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4164
    // Tracker ID: 46757
    fun sendClickBurgerButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click burger")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY_OLP_BMGM)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46757")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_OLP_BMGM)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_OLP_BMGM)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4164
    // Tracker ID: 46758
    fun sendClickSnkButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click snk")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY_OLP_BMGM)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46758")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_OLP_BMGM)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_OLP_BMGM)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4164
    // Tracker ID: 46759
    fun sendImpressSnkEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_VIEW_PG_IRIS)
            .setEventAction("impression snk bottomsheet")
            .setEventCategory("${TrackerConstant.EVENT_CATEGORY_OLP_BMGM} - snk")
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46759")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_OLP_BMGM)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_OLP_BMGM)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4164
    // Tracker ID: 46760
    fun sendClickCloseSnkButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click close snk")
            .setEventCategory("${TrackerConstant.EVENT_CATEGORY_OLP_BMGM} - snk")
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46760")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_OLP_BMGM)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_OLP_BMGM)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4164
    // Tracker ID: 46761
    fun sendClickFilterDropdownButtonEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click filter dropdown")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY_OLP_BMGM)
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46761")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_OLP_BMGM)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_OLP_BMGM)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4164
    // Tracker ID: 46762
    fun sendClickFilterButtonEvent(offerId: String, warehouseId: String, shopId: String, offerName: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click filter")
            .setEventCategory(TrackerConstant.EVENT_CATEGORY_OLP_BMGM)
            .setEventLabel(joinDash(offerId, warehouseId, offerName))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46762")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_OLP_BMGM)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_OLP_BMGM)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4164
    // Tracker ID: 46768
    fun sendImpressProductCardEvent(
        offerId: String,
        warehouseId: String,
        shopId: String,
        items: List<Map<String, Any>>,
        sortName: String
    ) {
        val bundle = bundleOf(
            TrackerConstant.EVENT to TrackerConstant.EVENT_VIEW_ITEM_LIST,
            TrackerConstant.EVENT_ACTION to "impression product card",
            TrackerConstant.EVENT_CATEGORY to TrackerConstant.EVENT_CATEGORY_OLP_BMGM,
            TrackerConstant.EVENT_LABEL to joinDash(offerId, warehouseId),
            TrackerConstant.TRACKER_ID to "46768",
            TrackerConstant.BUSINESS_UNIT to TrackerConstant.BUSINESS_UNIT_OLP_BMGM,
            TrackerConstant.CURRENT_SITE to TrackerConstant.CURRENT_SITE_OLP_BMGM,
            TrackerConstant.ITEM_LIST to joinDash("/bmsm_olp", sortName),
            TrackerConstant.ITEMS to items,
            TrackerConstant.SHOP_ID to shopId,
            TrackerConstant.USER_ID to userSession.userId
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TrackerConstant.EVENT_VIEW_ITEM_LIST, bundle)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4164
    // Tracker ID: 46769
    fun sendClickProductCardEvent(
        offerId: String,
        warehouseId: String,
        shopId: String,
        items: List<Map<String, Any>>,
        sortName: String
    ) {
        val bundle = bundleOf(
            TrackerConstant.EVENT to TrackerConstant.EVENT_SELECT_CONTENT,
            TrackerConstant.EVENT_ACTION to "click product card",
            TrackerConstant.EVENT_CATEGORY to TrackerConstant.EVENT_CATEGORY_OLP_BMGM,
            TrackerConstant.EVENT_LABEL to joinDash(offerId, warehouseId),
            TrackerConstant.TRACKER_ID to "46769",
            TrackerConstant.BUSINESS_UNIT to TrackerConstant.BUSINESS_UNIT_OLP_BMGM,
            TrackerConstant.CURRENT_SITE to TrackerConstant.CURRENT_SITE_OLP_BMGM,
            TrackerConstant.ITEM_LIST to joinDash("/bmsm_olp", sortName),
            TrackerConstant.ITEMS to items,
            TrackerConstant.SHOP_ID to shopId,
            TrackerConstant.USER_ID to userSession.userId
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TrackerConstant.EVENT_SELECT_CONTENT, bundle)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4164
    // Tracker ID: 46775
    fun sendClickAtcEvent(
        offerId: String,
        warehouseId: String,
        shopId: String,
        items: List<Map<String, Any>>
    ) {
        val bundle = bundleOf(
            TrackerConstant.EVENT to TrackerConstant.EVENT_ADD_TO_CART,
            TrackerConstant.EVENT_ACTION to "click atc",
            TrackerConstant.EVENT_CATEGORY to TrackerConstant.EVENT_CATEGORY_OLP_BMGM,
            TrackerConstant.EVENT_LABEL to joinDash(offerId, warehouseId),
            TrackerConstant.TRACKER_ID to "46775",
            TrackerConstant.BUSINESS_UNIT to TrackerConstant.BUSINESS_UNIT_OLP_BMGM,
            TrackerConstant.CURRENT_SITE to TrackerConstant.CURRENT_SITE_OLP_BMGM,
            TrackerConstant.ITEMS to items,
            TrackerConstant.SHOP_ID to shopId,
            TrackerConstant.USER_ID to userSession.userId
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TrackerConstant.EVENT_ADD_TO_CART, bundle)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4164
    // Tracker ID: 46777
    fun sendClickCloseVariantEvent(offerId: String, warehouseId: String, shopId: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT_CLICK_PG)
            .setEventAction("click close variant")
            .setEventCategory("${TrackerConstant.EVENT_CATEGORY_OLP_BMGM} - variant")
            .setEventLabel(joinDash(offerId, warehouseId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "46777")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT_OLP_BMGM)
            .setCurrentSite(TrackerConstant.CURRENT_SITE_OLP_BMGM)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    private fun joinDash(vararg s: String?): String {
        return TextUtils.join(" - ", s)
    }
}
