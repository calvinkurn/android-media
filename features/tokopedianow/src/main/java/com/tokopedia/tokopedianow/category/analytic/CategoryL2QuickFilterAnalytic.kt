package com.tokopedia.tokopedianow.category.analytic

import com.tokopedia.tokopedianow.category.analytic.CategoryL2Analytic.Companion.EVENT_CATEGORY_PAGE_L2
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_GROCERIES
import com.tokopedia.track.builder.Tracker

class CategoryL2QuickFilterAnalytic {

    companion object {
        // Event Action
        private const val EVENT_ACTION_CLICK_FULL_FILTER = "click full filter button"
        private const val EVENT_ACTION_CLICK_QUICK_FILTER = "click quick filter button"
        private const val EVENT_ACTION_CLICK_BRAND_NAVIGATION = "click brand navigational dropdown"

        // TackerId
        private const val TRACKER_ID_CLICK_FULL_FILTER = "43887"
        private const val TRACKER_ID_CLICK_QUICK_FILTER = "45249"
        private const val TRACKER_ID_CLICK_BRAND_NAVIGATION = "45250"
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 43887
    fun sendClickFullFilterButtonEvent(categoryIdL1: String, warehouseIds: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_FULL_FILTER)
            .setEventCategory(EVENT_CATEGORY_PAGE_L2)
            .setEventLabel(categoryIdL1)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_FULL_FILTER)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 45249
    fun sendClickQuickFilterButtonEvent(categoryIdL1: String, warehouseIds: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_QUICK_FILTER)
            .setEventCategory(EVENT_CATEGORY_PAGE_L2)
            .setEventLabel(categoryIdL1)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_QUICK_FILTER)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 45250
    fun sendClickBrandNavigationalDropdownEvent(categoryIdL1: String, warehouseIds: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_BRAND_NAVIGATION)
            .setEventCategory(EVENT_CATEGORY_PAGE_L2)
            .setEventLabel(categoryIdL1)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_BRAND_NAVIGATION)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }
}
