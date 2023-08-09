package com.tokopedia.tokopedianow.search.analytics

import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.joinDash
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.search.analytics.SearchEmptyNoResultAdultAnalytics.ACTION.EVENT_ACTION_CLICK_BUTTON_NO_RESULT_ADULT
import com.tokopedia.tokopedianow.search.analytics.SearchEmptyNoResultAdultAnalytics.ACTION.EVENT_ACTION_IMPRESSION_NO_RESULT_ADULT
import com.tokopedia.tokopedianow.search.analytics.SearchEmptyNoResultAdultAnalytics.CATEGORY.EVENT_CATEGORY_EMPTY_RESULT_PAGE
import com.tokopedia.tokopedianow.search.analytics.SearchEmptyNoResultAdultAnalytics.TRACKER_ID.TRACKER_ID_CLICK_BUTTON_NO_RESULT_ADULT
import com.tokopedia.tokopedianow.search.analytics.SearchEmptyNoResultAdultAnalytics.TRACKER_ID.TRACKER_ID_IMPRESSION_NO_RESULT_ADULT
import com.tokopedia.tokopedianow.search.analytics.SearchEmptyNoResultAdultAnalytics.VALUE.SEARCH_RESULT_PAGE
import com.tokopedia.track.builder.Tracker
import javax.inject.Inject

// Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4113
class SearchEmptyNoResultAdultAnalytics @Inject constructor(
    private val addressData: TokoNowLocalAddress
) {
    private object ACTION {
        const val EVENT_ACTION_IMPRESSION_NO_RESULT_ADULT = "impression no result for adult product"
        const val EVENT_ACTION_CLICK_BUTTON_NO_RESULT_ADULT = "click pelajari selengkapnya - no result for adult product"
    }

    private object CATEGORY {
        const val EVENT_CATEGORY_EMPTY_RESULT_PAGE = "tokonow - empty result page"
    }

    private object TRACKER_ID {
        const val TRACKER_ID_IMPRESSION_NO_RESULT_ADULT = "45611"
        const val TRACKER_ID_CLICK_BUTTON_NO_RESULT_ADULT = "45612"
    }

    private object VALUE {
        const val SEARCH_RESULT_PAGE = "search result page"
    }

    // Tracker ID: 45611
    fun sendImpressionNoResultForAdultProductEvent (
        keyword: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_GROCERIES)
            .setEventAction(EVENT_ACTION_IMPRESSION_NO_RESULT_ADULT)
            .setEventCategory(EVENT_CATEGORY_EMPTY_RESULT_PAGE)
            .setEventLabel(joinDash(SEARCH_RESULT_PAGE, keyword))
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_IMPRESSION_NO_RESULT_ADULT)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .setCustomProperty(KEY_WAREHOUSE_ID, addressData.getWarehouseId())
            .build()
            .send()
    }

    // Tracker ID: 45612
    fun sendClickLearnMoreNoResultForAdultProductEvent(
        keyword: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_BUTTON_NO_RESULT_ADULT)
            .setEventCategory(EVENT_CATEGORY_EMPTY_RESULT_PAGE)
            .setEventLabel(joinDash(SEARCH_RESULT_PAGE, keyword))
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_BUTTON_NO_RESULT_ADULT)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .setCustomProperty(KEY_WAREHOUSE_ID, addressData.getWarehouseId())
            .build()
            .send()
    }
}
