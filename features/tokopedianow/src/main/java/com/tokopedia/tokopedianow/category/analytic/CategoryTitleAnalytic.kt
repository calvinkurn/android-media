package com.tokopedia.tokopedianow.category.analytic

import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.ACTION.EVENT_ACTION_CLICK_OTHER_CATEGORIES
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.CATEGORY.EVENT_CATEGORY_PAGE_L1
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.TRACKER_ID.ID_CLICK_OTHER_CATEGORIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.joinDash
import com.tokopedia.track.builder.Tracker

/**
 * Category Revamp L1 Tracker
 * Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3980
 **/

class CategoryTitleAnalytic {
    // Tracker ID: 43848
    fun sendClickMoreCategoriesEvent (
        categoryIdL1: String,
        warehouseId: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_OTHER_CATEGORIES)
            .setEventCategory(EVENT_CATEGORY_PAGE_L1)
            .setEventLabel(joinDash(categoryIdL1, warehouseId))
            .setCustomProperty(KEY_TRACKER_ID, ID_CLICK_OTHER_CATEGORIES)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }
}
