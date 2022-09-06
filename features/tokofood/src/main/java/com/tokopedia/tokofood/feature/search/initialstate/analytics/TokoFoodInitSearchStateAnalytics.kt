package com.tokopedia.tokofood.feature.search.initialstate.analytics

import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class TokoFoodInitSearchStateAnalytics @Inject constructor() {

    private val tracker by lazy { TrackApp.getInstance().gtm }

    fun impressViewSearchHistory(keyword: String) {
        val eventData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.VIEW_PG_IRIS,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.VIEW_SEARCH_HISTORY_TOKOFOOD,
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION,
            TrackAppUtils.EVENT_LABEL to keyword,
            TokoFoodAnalyticsConstants.TRACKER_ID to TokoFoodAnalyticsConstants.TRACKER_ID_35767,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(eventData)
    }

    fun clickSearchHistory(keyword: String) {
        val eventData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.CLICK_PG,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.CLICK_SEARCH_HISTORY_TOKOFOOD,
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION,
            TrackAppUtils.EVENT_LABEL to keyword,
            TokoFoodAnalyticsConstants.TRACKER_ID to TokoFoodAnalyticsConstants.TRACKER_ID_35768,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(eventData)
    }

    fun impressViewTopKeyword(keyword: String) {
        val eventData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.VIEW_PG_IRIS,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.VIEW_TOP_KEYWORD_TOKOFOOD,
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION,
            TrackAppUtils.EVENT_LABEL to keyword,
            TokoFoodAnalyticsConstants.TRACKER_ID to TokoFoodAnalyticsConstants.TRACKER_ID_35769,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(eventData)
    }

    fun clickTopKeyword(keyword: String) {
        val eventData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.CLICK_PG,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.CLICK_TOP_KEYWORD_TOKOFOOD,
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION,
            TrackAppUtils.EVENT_LABEL to keyword,
            TokoFoodAnalyticsConstants.TRACKER_ID to TokoFoodAnalyticsConstants.TRACKER_ID_35770,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(eventData)
    }

    fun impressViewCuisineList(keyword: String) {
        val eventData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.VIEW_PG_IRIS,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.VIEW_CUISINE_LIST_TOKOFOOD,
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION,
            TrackAppUtils.EVENT_LABEL to keyword,
            TokoFoodAnalyticsConstants.TRACKER_ID to TokoFoodAnalyticsConstants.TRACKER_ID_35771,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(eventData)
    }

    fun clickCuisineList(keyword: String) {
        val eventData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.CLICK_PG,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.CLICK_CUISINE_LIST_TOKOFOOD,
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION,
            TrackAppUtils.EVENT_LABEL to keyword,
            TokoFoodAnalyticsConstants.TRACKER_ID to TokoFoodAnalyticsConstants.TRACKER_ID_35772,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(eventData)
    }
}