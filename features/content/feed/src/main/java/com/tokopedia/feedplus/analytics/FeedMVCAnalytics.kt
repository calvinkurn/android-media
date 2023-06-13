package com.tokopedia.feedplus.analytics

import com.tokopedia.feedplus.analytics.FeedAnalytics.Companion.getContentType
import com.tokopedia.feedplus.analytics.FeedAnalytics.Companion.getPostType
import com.tokopedia.feedplus.analytics.FeedAnalytics.Companion.getPrefix
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.mvcwidget.trackers.DefaultMvcTrackerImpl
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.track.TrackApp

/**
 * Created By : Muhammad Furqan on 03/05/23
 */
class FeedMVCAnalytics : DefaultMvcTrackerImpl() {

    var trackerData: FeedTrackerDataModel? = null

    override fun userClickEntryPoints(
        shopId: String,
        userId: String?,
        @MvcSource source: Int,
        isTokomember: Boolean,
        productId: String
    ) {
        val eventLabel = trackerData?.let {
            "${it.activityId} - ${it.authorId} - ${getPrefix(it.tabType)} - ${
                getPostType(
                    it.typename,
                    it.type,
                    it.authorType,
                    it.isFollowing
                )
            } - ${
                getContentType(
                    it.typename,
                    it.type,
                    it.mediaType
                )
            } - ${it.contentScore} - ${it.hasVoucher} - ${it.campaignStatus} - ${it.entryPoint}"
        } ?: ""

        val map = mapOf(
            KEY_EVENT to EVENT,
            KEY_EVENT_CATEGORY to EVENT_CATEGORY,
            KEY_EVENT_ACTION to EVENT_ACTION,
            KEY_EVENT_LABEL to eventLabel,
            KEY_TRACKER_ID to "41607",
            KEY_BUSINESS_UNIT to BUSINESS_UNIT_CONTENT,
            KEY_CURRENT_SITE to CURRENT_SITE_MARKETPLACE
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    companion object {
        private const val KEY_EVENT = "event"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"
        private const val KEY_TRACKER_ID = "trackerId"
        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"

        private const val EVENT = "select_content"
        private const val EVENT_CATEGORY = "unified feed"
        private const val EVENT_ACTION = "click - voucher bottomsheet"
        private const val BUSINESS_UNIT_CONTENT = "content"
        private const val CURRENT_SITE_MARKETPLACE = "tokopediamarketplace"
    }
}
