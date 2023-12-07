package com.tokopedia.home_explore_category.analytic

import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.ALL_CATEGORY_PAGE
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.CLICK_ACCORDION
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.CLICK_HOMEPAGE
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.HOME_AND_BROWSE
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.KEY_BUSINESS_UNIT
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.KEY_CURRENT_SITE
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.KEY_TRACKER_ID
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.TOKOPEDIA_MARKETPLACE
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.TRACKER_ID_47052
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ExploreCategoryAnalytics @Inject constructor(
    private val userSession: UserSessionInterface
) {

    private fun createEventMap(
        event: String,
        category: String,
        action: String,
        label: String
    ): HashMap<String, Any> {
        val eventMap = HashMap<String, Any>()
        eventMap[EVENT] = event
        eventMap[EVENT_CATEGORY] = category
        eventMap[EVENT_ACTION] = action
        eventMap[EVENT_LABEL] = label
        return eventMap
    }

    fun sendCategoryItemClicked(categoryName: String, userId: String) {
        val event = createEventMap(
            event = CLICK_HOMEPAGE,
            category = ALL_CATEGORY_PAGE,
            action = CLICK_ACCORDION,
            label = categoryName
        ).apply {
            put(KEY_TRACKER_ID, TRACKER_ID_47052)
            put(KEY_BUSINESS_UNIT, HOME_AND_BROWSE)
            put(KEY_CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun sendSubCategoryItemClicked(categoryName: String, userId: String) {
        val event = createEventMap(
            event = CLICK_HOMEPAGE,
            category = ALL_CATEGORY_PAGE,
            action = CLICK_ACCORDION,
            label = categoryName
        ).apply {
            put(KEY_TRACKER_ID, TRACKER_ID_47052)
            put(KEY_BUSINESS_UNIT, HOME_AND_BROWSE)
            put(KEY_CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }
}
