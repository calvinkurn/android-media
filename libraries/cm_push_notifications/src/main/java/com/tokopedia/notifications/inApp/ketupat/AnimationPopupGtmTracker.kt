package com.tokopedia.notifications.inApp.ketupat

import com.tokopedia.notifications.common.CMConstant.AnimationGtmTrackerEvents
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics

class AnimationPopupGtmTracker {

    private val analyticsTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    private var eventLabel = ""

    fun sendPopupImpressionEvent(scratchCardId: String) {
        eventLabel = "{direct_reward_id: $scratchCardId}"
        createMapAndSendEvent(
            VIEW_LG_IRIS,
            AnimationGtmTrackerEvents.VALUE_IMPRESSION_POPUP_EVENT_ACTION,
            eventLabel,
            AnimationGtmTrackerEvents.VALUE_IMPRESSION_TRACKER_ID
            )
    }

    fun sendPopupInteractionEvent(scratchCardId: String) {
        eventLabel = "{direct_reward_id: $scratchCardId}"
        createMapAndSendEvent(
            CLICK_LG_IRIS,
            AnimationGtmTrackerEvents.VALUE_INTERACTION_POPUP_EVENT_ACTION,
            eventLabel,
            AnimationGtmTrackerEvents.VALUE_INTERACTION_TRACKER_ID
        )
    }

    fun sendPopupCloseEvent(scratchCardId: String) {
        eventLabel = "{direct_reward_id: $scratchCardId}"
        createMapAndSendEvent(
            CLICK_LG_IRIS,
            AnimationGtmTrackerEvents.VALUE_CLOSE_POPUP_EVENT_ACTION,
            eventLabel,
            AnimationGtmTrackerEvents.VALUE_CLOSE_TRACKER_ID
        )
    }

    fun sendCouponImpressionEvent(scratchCardId: String, catalogSlug: String, catalogId: String) {
        eventLabel = "{direct_reward_id: $scratchCardId, catalog_slug:$catalogSlug, catalog_id:$catalogId}"
        createMapAndSendEvent(
            CLICK_LG_IRIS,
            AnimationGtmTrackerEvents.VALUE_IMPRESSION_COUPON_EVENT_ACTION,
            eventLabel,
            AnimationGtmTrackerEvents.VALUE_IMPRESSION_COUPON_TRACKER_ID
        )
    }

    fun sendCtaButtonImpressionEvent(scratchCardId: String, catalogSlug: String, catalogId: String) {
        eventLabel = "{direct_reward_id: $scratchCardId, catalog_slug:$catalogSlug, catalog_id:$catalogId}"
        createMapAndSendEvent(
            VIEW_LG_IRIS,
            AnimationGtmTrackerEvents.VALUE_IMPRESSION_CTA_BUTTON_EVENT_ACTION,
            eventLabel,
            AnimationGtmTrackerEvents.VALUE_IMPRESSION_CTA_BUTTON_TRACKER_ID
        )
    }

    fun sendCtaButtonClickEvent(scratchCardId: String, catalogSlug: String, catalogId: String) {
        eventLabel = "{direct_reward_id: $scratchCardId, catalog_slug:$catalogSlug, catalog_id:$catalogId}"
        createMapAndSendEvent(
            CLICK_LG_IRIS,
            AnimationGtmTrackerEvents.VALUE_CLICK_CTA_BUTTON_EVENT_ACTION,
            eventLabel,
            AnimationGtmTrackerEvents.VALUE_CLICK_CTA_BUTTON_TRACKER_ID
        )
    }

    fun sendErrorImpressionEvent(scratchCardId: String) {
        eventLabel = "{direct_reward_id: $scratchCardId}"
        createMapAndSendEvent(
            VIEW_LG_IRIS,
            AnimationGtmTrackerEvents.VALUE_IMPRESSION_ERROR_EVENT_ACTION,
            eventLabel,
            AnimationGtmTrackerEvents.VALUE_IMPRESSION_ERROR_TRACKER_ID
        )
    }

    fun sendErrorRetryButtonEvent(scratchCardId: String) {
        eventLabel = "{direct_reward_id: $scratchCardId}"
        createMapAndSendEvent(
            CLICK_LG_IRIS,
            AnimationGtmTrackerEvents.VALUE_CLICK_RETRY_BUTTON_EVENT_ACTION,
            eventLabel,
            AnimationGtmTrackerEvents.VALUE_CLICK_RETRY_BUTTON_TRACKER_ID
        )
    }

    fun sendErrorUnRetryableImpressionEvent(scratchCardId: String) {
        eventLabel = "{direct_reward_id: $scratchCardId}"
        createMapAndSendEvent(
            VIEW_LG_IRIS,
            AnimationGtmTrackerEvents.VALUE_IMPRESSION_UNRETRY_ERROR_EVENT_ACTION,
            eventLabel,
            AnimationGtmTrackerEvents.VALUE_IMPRESSION_UNRETRY_ERROR_TRACKER_ID
        )
    }

    private fun createMapAndSendEvent(
        event: String,
        eventAction: String,
        eventLabel: String,
        trackerId: String,
        businessUnit: String = "gamification",
        currentSite: String = "tokopediamarketplace"
    ) {
        val map = TrackAppUtils.gtmData(
            event,
            AnimationGtmTrackerEvents.VALUE_EVENT_CATEGORY_REWARD_POPUP,
            eventAction,
            eventLabel
        )
        map[CMConstant.GtmTrackerEvents.KEY_TRACKER_ID] = trackerId
        map[CMConstant.GtmTrackerEvents.KEY_BUSINESS_UNIT] = businessUnit
        map[CMConstant.GtmTrackerEvents.KEY_CURRENT_SITE] = currentSite

        analyticsTracker.sendGeneralEvent(map)
    }

    companion object {
        const val VIEW_LG_IRIS = "viewLGIris"
        const val CLICK_LG_IRIS = "clickLG"
    }
}
