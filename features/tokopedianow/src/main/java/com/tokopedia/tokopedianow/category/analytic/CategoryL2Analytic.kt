package com.tokopedia.tokopedianow.category.analytic

import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_GROCERIES
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CategoryL2Analytic @Inject constructor(userSession: UserSessionInterface) {

    companion object {

        // Event Action
        private const val EVENT_ACTION_CLICK_OTHER_CATEGORIES = "click kategori lain button"
        private const val EVENT_ACTION_IMPRESSION_CATEGORY_NAVIGATION = "impression l2 category navigation"
        private const val EVENT_ACTION_CLICK_L2_NAVIGATION = "click l2 category navigation"

        // Event Category
        const val EVENT_CATEGORY_PAGE_L2 = "tokonow - category page - l2"

        // TrackerId
        private const val TRACKER_ID_CLICK_OTHER_CATEGORIES = "43878"
        private const val TRACKER_ID_IMPRESSION_CATEGORY_NAVIGATION = "43879"
        private const val TRACKER_ID_CLICK_CATEGORY_NAVIGATION = "43880"
    }

    val adsProductAnalytic = CategoryL2ProductAdsAnalytic(userSession)
    val productAnalytic = CategoryL2ProductAnalytic(userSession)
    val quickFilterAnalytic = CategoryL2QuickFilterAnalytic()
    val sortFilterAnalytic = CategoryL2SortFilterAnalytic()

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 43878
    fun sendClickOtherCategoriesEvent(
        categoryIdL1: String,
        warehouseIds: String
    ) {

        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_OTHER_CATEGORIES)
            .setEventCategory(EVENT_CATEGORY_PAGE_L2)
            .setEventLabel(categoryIdL1)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_OTHER_CATEGORIES)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 43879
    fun sendImpressionLCategoryNavigationEvent(
        categoryIdL1: String,
        categoryIdL2: String,
        warehouseIds: String
    ) {
        val eventLabel = "$categoryIdL1 - $categoryIdL2"

        Tracker.Builder()
            .setEvent(EVENT_VIEW_GROCERIES)
            .setEventAction(EVENT_ACTION_IMPRESSION_CATEGORY_NAVIGATION)
            .setEventCategory(EVENT_CATEGORY_PAGE_L2)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_IMPRESSION_CATEGORY_NAVIGATION)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 43880
    fun sendClickLCategoryNavigationEvent(
        categoryIdL1: String,
        categoryIdL2: String,
        warehouseIds: String
    ) {
        val eventLabel = "$categoryIdL1 - $categoryIdL2"

        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_L2_NAVIGATION)
            .setEventCategory(EVENT_CATEGORY_PAGE_L2)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_CATEGORY_NAVIGATION)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }
}
