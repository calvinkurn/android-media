package com.tokopedia.tokopedianow.category.analytic

import com.tokopedia.tokopedianow.category.analytic.CategoryL2Analytic.Companion.EVENT_CATEGORY_PAGE_L2
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_GROCERIES
import com.tokopedia.track.builder.Tracker

class CategoryL2TickerAnalytic {

    companion object {

        // Event Action
        private const val EVENT_ACTION_IMPRESSION_OOS_TICKER = "impression oos ticker"
        private const val EVENT_ACTION_CLICK_CLOSE_OOS_TICKER = "click close button on oos ticker"

        // TrackerId
        private const val TRACKER_ID_IMPRESSION_OOS_TICKER = "45267"
        private const val TRACKER_ID_CLICK_CLOSE_OOS_TICKER = "45268"
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 45267
    fun sendImpressionOosTickerEvent(categoryIdL2: String, warehouseIds: String) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_GROCERIES)
            .setEventAction(EVENT_ACTION_IMPRESSION_OOS_TICKER)
            .setEventCategory(EVENT_CATEGORY_PAGE_L2)
            .setEventLabel(categoryIdL2)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_IMPRESSION_OOS_TICKER)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 45268
    fun sendClickCloseButtonOnOosTickerEvent(categoryIdL2: String, warehouseIds: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_CLOSE_OOS_TICKER)
            .setEventCategory(EVENT_CATEGORY_PAGE_L2)
            .setEventLabel(categoryIdL2)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_CLOSE_OOS_TICKER)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }
}
