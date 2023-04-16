package com.tokopedia.feedplus.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 14/04/23
 */
class FeedAnalytics @Inject constructor(
    private val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface
) {

    companion object {
        const val KEY_EVENT_USER_ID = "userId"
        const val KEY_BUSINESS_UNIT_EVENT = "businessUnit"
        const val KEY_CURRENT_SITE_EVENT = "currentSite"
        const val KEY_TRACKER_ID = "trackerId"

        const val BUSINESS_UNIT_CONTENT = "content"
        const val CURRENT_SITE_MARKETPLACE = "tokopediamarketplace"
        const val CATEGORY_UNIFIED_FEED = "unified feed"
    }

    private object Event {
        const val VIEW_ITEM = "view_item"
    }

    private object Category {
        const val UNIFIED_FEED = "unified feed"
    }

    private object Action {
        const val VIEW_POST = "view - post"
    }

    private object Promotion {

    }

    private fun generateGeneralTrackerData(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String
    ): Map<String, Any> = DataLayer.mapOf(
        EVENT, eventName,
        EVENT_CATEGORY, eventCategory,
        EVENT_ACTION, eventAction,
        EVENT_LABEL, eventLabel,
        KEY_EVENT_USER_ID, userSession.userId,
        KEY_BUSINESS_UNIT_EVENT, BUSINESS_UNIT_CONTENT,
        KEY_CURRENT_SITE_EVENT, CURRENT_SITE_MARKETPLACE
    )

}
