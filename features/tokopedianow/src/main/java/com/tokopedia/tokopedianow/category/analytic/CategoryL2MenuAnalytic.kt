package com.tokopedia.tokopedianow.category.analytic

import com.tokopedia.tokopedianow.category.analytic.CategoryL2Analytic.Companion.EVENT_CATEGORY_PAGE_L2
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_GROCERIES
import com.tokopedia.track.builder.Tracker

class CategoryL2MenuAnalytic {

    companion object {

        private const val EVENT_IMPRESSION_CATEGORY_RECOM_WIDGET = "impression category recom widget"
        private const val EVENT_CLICK_CATEGORY_RECOM_WIDGET = "click category recom widget"

        private const val TRACKER_ID_IMPRESSION_CATEGORY_RECOM_WIDGET = "47700"
        private const val TRACKER_ID_CLICK_CATEGORY_RECOM_WIDGET = "47701"
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 47700
    fun sendImpressionCategoryRecomWidgetEvent(
        categoryL1Id: String,
        categoryL2Id: String,
        headerName: String,
        warehouseIds: String
    ) {
        val eventLabel = "$categoryL2Id - $categoryL1Id - $headerName"

        Tracker.Builder()
            .setEvent(EVENT_VIEW_GROCERIES)
            .setEventAction(EVENT_IMPRESSION_CATEGORY_RECOM_WIDGET)
            .setEventCategory(EVENT_CATEGORY_PAGE_L2)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_IMPRESSION_CATEGORY_RECOM_WIDGET)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 47701
    fun sendClickCategoryRecomWidgetEvent(
        categoryL1Id: String,
        categoryL2Id: String,
        headerName: String,
        warehouseIds: String
    ) {
        val eventLabel = "$categoryL2Id - $categoryL1Id - $headerName"

        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_CLICK_CATEGORY_RECOM_WIDGET)
            .setEventCategory(EVENT_CATEGORY_PAGE_L2)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_CATEGORY_RECOM_WIDGET)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }
}
