package com.tokopedia.reviewseller.feature.inboxreview.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.CLICK_IN_FULL_REVIEW
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.CLICK_NOT_REPLIED_REVIEW
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.CLICK_OUTSIDE_FILTER_RATING_BOTTOM_SHEET
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.CLICK_QUICK_FILTER
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.CLICK_REPLIED_REVIEW
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.CLICK_RESET_ON_FILTER_RATING_BOTTOM_SHEET
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.CLICK_REVIEW
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.CLICK_REVIEW_ATTACHMENT
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.CLICK_SELECT_ON_FILTER_RATING_BOTTOM_SHEET
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.CLICK_STAR_RATING_ON_FILTER_BOTTOM_SHEET
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.CLICK_STAR_RATING_QUICK_FILTER
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.EVENT
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.EVENT_ACTION
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.EVENT_CATEGORY
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.EVENT_LABEL
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.FEEDBACK_ID
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.INBOX_REVIEW
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.IS_ACTIVE
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.PRODUCT_ID
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.QUICK_FILTER
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.REVIEW_INBOX
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.SCREEN_NAME
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.SHOP_ID
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.STAR_SELECTED
import com.tokopedia.reviewseller.feature.inboxreview.analytics.InboxReviewTrackingConstant.USER_ID
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

/*
Review Seller Revamp
Data layer docs
https://docs.google.com/spreadsheets/d/1V8oaqxfu7A-rUq9BtZKxqP0Gy2WHFumiGNzh5piX9TI/edit#gid=1022075175
 */

object InboxReviewTracking {

    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }

    fun openScreenInboxReview(shopId: String, userId: String) {
        val dataLayer = mutableMapOf<String, String>()
        dataLayer[SCREEN_NAME] = REVIEW_INBOX
        dataLayer[USER_ID] = userId
        dataLayer[SHOP_ID] = shopId
        tracker.sendScreenAuthenticated(REVIEW_INBOX, dataLayer)
    }

    fun eventClickReviewNotYetReplied(feedbackId: String, quickFilter: String, shopId: String, productId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_REVIEW,
                EVENT_CATEGORY, INBOX_REVIEW,
                EVENT_ACTION, CLICK_NOT_REPLIED_REVIEW,
                EVENT_LABEL, "$FEEDBACK_ID:$feedbackId; $QUICK_FILTER:$quickFilter;",
                SCREEN_NAME, REVIEW_INBOX,
                SHOP_ID, shopId,
                PRODUCT_ID, productId
        ))
    }

    fun eventClickReviewReplied(feedbackId: String, quickFilter: String, shopId: String, productId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_REVIEW,
                EVENT_CATEGORY, INBOX_REVIEW,
                EVENT_ACTION, CLICK_REPLIED_REVIEW,
                EVENT_LABEL, "$FEEDBACK_ID:$feedbackId; $QUICK_FILTER:$quickFilter;",
                SCREEN_NAME, REVIEW_INBOX,
                SHOP_ID, shopId,
                PRODUCT_ID, productId
        ))
    }

    fun eventClickSpecificImageReview(feedbackId: String, quickFilter: String, shopId: String, productId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_REVIEW,
                EVENT_CATEGORY, INBOX_REVIEW,
                EVENT_ACTION, CLICK_REVIEW_ATTACHMENT,
                EVENT_LABEL, "$FEEDBACK_ID:$feedbackId; $QUICK_FILTER:$quickFilter;",
                SCREEN_NAME, REVIEW_INBOX,
                SHOP_ID, shopId,
                PRODUCT_ID, productId
        ))
    }

    fun eventClickInFullReview(feedbackId: String, quickFilter: String, shopId: String, productId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_REVIEW,
                EVENT_CATEGORY, INBOX_REVIEW,
                EVENT_ACTION, CLICK_IN_FULL_REVIEW,
                EVENT_LABEL, "$FEEDBACK_ID:$feedbackId; $QUICK_FILTER:$quickFilter;",
                SCREEN_NAME, REVIEW_INBOX,
                SHOP_ID, shopId,
                PRODUCT_ID, productId
        ))
    }

    fun eventClickHasBeenRepliedFilter(quickFilter: String, isActive: String, shopId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_REVIEW,
                EVENT_CATEGORY, INBOX_REVIEW,
                EVENT_ACTION, CLICK_QUICK_FILTER,
                EVENT_LABEL, "$QUICK_FILTER:$quickFilter; $IS_ACTIVE:$isActive;",
                SCREEN_NAME, REVIEW_INBOX,
                SHOP_ID, shopId))
    }

    fun eventClickNotRepliedFilter(quickFilter: String, isActive: String, shopId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_REVIEW,
                EVENT_CATEGORY, INBOX_REVIEW,
                EVENT_ACTION, CLICK_QUICK_FILTER,
                EVENT_LABEL, "$QUICK_FILTER:$quickFilter; $IS_ACTIVE:$isActive;",
                SCREEN_NAME, REVIEW_INBOX,
                SHOP_ID, shopId
        ))
    }

    fun eventClickRatingFilter(shopId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_REVIEW,
                EVENT_CATEGORY, INBOX_REVIEW,
                EVENT_ACTION, CLICK_STAR_RATING_QUICK_FILTER,
                EVENT_LABEL, "",
                SCREEN_NAME, REVIEW_INBOX,
                SHOP_ID, shopId
        ))
    }

    fun eventClickItemRatingFilterBottomSheet(starSelected: String, isActive: String, shopId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_REVIEW,
                EVENT_CATEGORY, INBOX_REVIEW,
                EVENT_ACTION, CLICK_STAR_RATING_ON_FILTER_BOTTOM_SHEET,
                EVENT_LABEL, "$STAR_SELECTED:$starSelected;$IS_ACTIVE:$isActive;",
                SCREEN_NAME, REVIEW_INBOX,
                SHOP_ID, shopId
        ))
    }

    fun eventClickSelectedRatingFilterBottomSheet(starSelected: String, shopId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_REVIEW,
                EVENT_CATEGORY, INBOX_REVIEW,
                EVENT_ACTION, CLICK_SELECT_ON_FILTER_RATING_BOTTOM_SHEET,
                EVENT_LABEL, "$STAR_SELECTED:$starSelected;",
                SCREEN_NAME, REVIEW_INBOX,
                SHOP_ID, shopId
        ))
    }

    fun eventClickResetRatingFilterBottomSheet(starSelected: String, shopId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_REVIEW,
                EVENT_CATEGORY, INBOX_REVIEW,
                EVENT_ACTION, CLICK_RESET_ON_FILTER_RATING_BOTTOM_SHEET,
                EVENT_LABEL, "$STAR_SELECTED:$starSelected;",
                SCREEN_NAME, REVIEW_INBOX,
                SHOP_ID, shopId
        ))
    }

    fun eventClickOutsideRatingFilterBottomSheet(shopId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_REVIEW,
                EVENT_CATEGORY, INBOX_REVIEW,
                EVENT_ACTION, CLICK_OUTSIDE_FILTER_RATING_BOTTOM_SHEET,
                EVENT_LABEL, "",
                SCREEN_NAME, REVIEW_INBOX,
                SHOP_ID, shopId
        ))
    }
}