package com.tokopedia.sellerhome.settings.analytics

import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.sellerhome.analytic.TrackingHelper

/**
 * Tracker link: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/2559
 */
object SocialMediaLinksTracker {

    private const val VIEW_PG_IRIS = "viewPGIris"
    private const val CLICK_PG = "clickPG"

    object EventAction {
        const val IMPRESSION_SOCMED_SHEET = "impression socmed sheet"
        const val CLICK_TOKOPEDIA_SELLER_SOCMED = "click tokopedia seller socmed"
        const val CLICK_IG = "$CLICK_TOKOPEDIA_SELLER_SOCMED - instagram"
        const val CLICK_YT = "$CLICK_TOKOPEDIA_SELLER_SOCMED - youtube"
        const val CLICK_FB = "$CLICK_TOKOPEDIA_SELLER_SOCMED - facebook"
        const val CLICK_COM_FB = "$CLICK_TOKOPEDIA_SELLER_SOCMED - community facebook"
    }

    fun sendBottomSheetImpressionEvent() {
        val eventMap = createEventMap(
            event = VIEW_PG_IRIS,
            action = EventAction.IMPRESSION_SOCMED_SHEET
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendClickEvent(eventAction: String = EventAction.CLICK_TOKOPEDIA_SELLER_SOCMED) {
        val eventMap = createEventMap(action = eventAction)
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    private fun createEventMap(
        event: String = CLICK_PG,
        category: String = SettingTrackingConstant.SETTINGS,
        action: String,
        label: String = TrackingConstant.EMPTY_STRING
    ): MutableMap<String, Any> {
        val map = TrackingHelper.createMap(event, category, action, label)
        map[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS_CAPITALIZED
        map[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIA_MARKETPLACE
        return map
    }

}