package com.tokopedia.tokopedianow.home.analytic

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getTracker
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.CATEGORY.EVENT_CATEGORY_HOME_PAGE
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import javax.inject.Inject

class HomeQuestWidgetAnalytics @Inject constructor() {
    companion object {
        private const val EVENT_ACTION_IMPRESSION_QUEST_WIDGET = "impression quest widget"
        private const val EVENT_ACTION_CLICK_SEE_DETAILS_QUEST_WIDGET = "click see details quest widget"
        private const val EVENT_ACTION_CLICK_QUEST_CARD_QUEST_WIDGET = "click quest card on quest widget"
        private const val EVENT_ACTION_IMPRESSION_FINISHED_QUEST_WIDGET = "impression finish card quest widget"
        private const val EVENT_ACTION_CLICK_PROGRESSIVE_BAR_QUEST_WIDGET = "click progressive bar on quest widget"
        private const val EVENT_ACTION_IMPRESSION_SWIPE_QUEST_CARD_QUEST_WIDGET = "impression swipe quest card on quest widget"

        private const val TRACKER_ID_IMPRESSION_QUEST_WIDGET = "26995"
        private const val TRACKER_ID_CLICK_SEE_DETAILS_QUEST_WIDGET= "26996"
        private const val TRACKER_ID_CLICK_QUEST_CARD_QUEST_WIDGET = "26998"
        private const val TRACKER_ID_IMPRESSION_FINISHED_QUEST_WIDGET = "26999"
        private const val TRACKER_ID_CLICK_PROGRESSIVE_BAR_QUEST_WIDGET = "48157"
        private const val TRACKER_ID_IMPRESSION_SWIPE_QUEST_CARD_QUEST_WIDGET = "48158"
    }

    private fun getMarketplaceDataLayer(
        event: String,
        action: String,
        label: String = String.EMPTY,
        trackerId: String
    ): Bundle {
        return Bundle().apply {
            putString(EVENT, event)
            putString(EVENT_ACTION, action)
            putString(EVENT_CATEGORY, EVENT_CATEGORY_HOME_PAGE)
            putString(EVENT_LABEL, label)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_TRACKER_ID, trackerId)
        }
    }

    fun trackImpressionQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_GROCERIES,
            action = EVENT_ACTION_IMPRESSION_QUEST_WIDGET,
            trackerId = TRACKER_ID_IMPRESSION_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_GROCERIES, dataLayer)
    }

    fun trackClickSeeDetailsQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_CLICK_GROCERIES,
            action = EVENT_ACTION_CLICK_SEE_DETAILS_QUEST_WIDGET,
            trackerId = TRACKER_ID_CLICK_SEE_DETAILS_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_CLICK_GROCERIES, dataLayer)
    }

    fun trackClickCardQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_CLICK_GROCERIES,
            action = EVENT_ACTION_CLICK_QUEST_CARD_QUEST_WIDGET,
            trackerId = TRACKER_ID_CLICK_QUEST_CARD_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_CLICK_GROCERIES, dataLayer)
    }

    fun trackImpressionFinishedQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_GROCERIES,
            action = EVENT_ACTION_IMPRESSION_FINISHED_QUEST_WIDGET,
            trackerId = TRACKER_ID_IMPRESSION_FINISHED_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_GROCERIES, dataLayer)
    }

    fun trackClickProgressiveBarQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_CLICK_GROCERIES,
            action = EVENT_ACTION_CLICK_PROGRESSIVE_BAR_QUEST_WIDGET,
            trackerId = TRACKER_ID_CLICK_PROGRESSIVE_BAR_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_CLICK_GROCERIES, dataLayer)
    }

    fun trackImpressionSwipeQuestCardQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_GROCERIES,
            action = EVENT_ACTION_IMPRESSION_SWIPE_QUEST_CARD_QUEST_WIDGET,
            trackerId = TRACKER_ID_IMPRESSION_SWIPE_QUEST_CARD_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_GROCERIES, dataLayer)
    }
}
