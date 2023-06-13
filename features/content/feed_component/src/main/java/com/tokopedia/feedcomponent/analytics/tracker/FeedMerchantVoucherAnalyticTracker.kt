package com.tokopedia.feedcomponent.analytics.tracker

import com.tokopedia.mvcwidget.trackers.DefaultMvcTrackerImpl
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.track.TrackApp

/**
 * Created By : Muhammad Furqan on 01/11/22
 */
class FeedMerchantVoucherAnalyticTracker :
    DefaultMvcTrackerImpl() {

    var activityId: String = ""
    var contentScore: String = ""
    var status: String = "" // pre / ongoing
    var hasVoucher: Boolean = false

    override fun userClickEntryPoints(
        shopId: String,
        userId: String?,
        @MvcSource source: Int,
        isTokomember: Boolean,
        productId: String
    ) {
        val eventCategory = when (source) {
            MvcSource.FEED_BOTTOM_SHEET -> "$EVENT_CATEGORY_PREFIX - $EVENT_CATEGORY_BOTTOM_SHEET"
            MvcSource.FEED_PRODUCT_DETAIL -> "$EVENT_CATEGORY_PREFIX - $EVENT_CATEGORY_PRODUCT_DETAIL"
            else -> EVENT_CATEGORY_PREFIX
        }
        val trackerId = when (source) {
            MvcSource.FEED_BOTTOM_SHEET -> TRACKER_ID_BOTTOM_SHEET
            MvcSource.FEED_PRODUCT_DETAIL -> TRACKER_ID_PRODUCT_DETAIL
            else -> ""
        }
        val eventLabel = "$activityId - $shopId - $contentScore - $status - $hasVoucher"

        val map = mapOf(
            KEY_EVENT to EVENT,
            KEY_EVENT_CATEGORY to eventCategory,
            KEY_EVENT_ACTION to EVENT_ACTION,
            KEY_EVENT_LABEL to eventLabel,
            KEY_TRACKER_ID to trackerId,
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

        private const val EVENT = "clickFeed"
        private const val EVENT_ACTION = "click - merchant voucher - asgc"
        private const val BUSINESS_UNIT_CONTENT = "content"
        private const val CURRENT_SITE_MARKETPLACE = "tokopediamarketplace"

        private const val EVENT_CATEGORY_PREFIX = "content feed timeline"
        private const val EVENT_CATEGORY_BOTTOM_SHEET = "bottom sheet"
        private const val EVENT_CATEGORY_PRODUCT_DETAIL = "product detail"

        private const val TRACKER_ID_BOTTOM_SHEET = "37934"
        private const val TRACKER_ID_PRODUCT_DETAIL = "37935"
    }
}
