package com.tokopedia.tokopedianow.category.analytic

import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.ACTION.EVENT_ACTION_CLICK_CATEGORY_L2
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.ACTION.EVENT_ACTION_IMPRESS_CATEGORY_L2
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.CATEGORY.EVENT_CATEGORY_PAGE_L1
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.TRACKER_ID.ID_CLICK_CATEGORY_L2
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.TRACKER_ID.ID_IMPRESS_CATEGORY_L2
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.NULL
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.joinDash
import com.tokopedia.track.builder.Tracker

/**
 * Category Revamp L1 Tracker
 * Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3980
 **/

class CategoryNavigationAnalytic {
    // Tracker ID: 43849
    fun sendImpressionCategoryNavigationEvent (
        categoryIdL1: String,
        categoryIdL2: String,
        warehouseId: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_GROCERIES)
            .setEventAction(EVENT_ACTION_IMPRESS_CATEGORY_L2)
            .setEventCategory(EVENT_CATEGORY_PAGE_L1)
            .setEventLabel(joinDash(categoryIdL1, categoryIdL2, NULL))
            .setCustomProperty(KEY_TRACKER_ID, ID_IMPRESS_CATEGORY_L2)
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseId)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    // Tracker ID: 43850
    fun sendClickCategoryNavigationEvent (
        categoryIdL1: String,
        categoryIdL2: String,
        warehouseId: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_CATEGORY_L2)
            .setEventCategory(EVENT_CATEGORY_PAGE_L1)
            .setEventLabel(joinDash(categoryIdL1, categoryIdL2, NULL))
            .setCustomProperty(KEY_TRACKER_ID, ID_CLICK_CATEGORY_L2)
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseId)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }
}
