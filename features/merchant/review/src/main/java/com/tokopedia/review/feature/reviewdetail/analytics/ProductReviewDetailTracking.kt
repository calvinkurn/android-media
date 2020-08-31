package com.tokopedia.review.feature.reviewdetail.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

/*
Review Seller Revamp
Data layer docs
https://docs.google.com/spreadsheets/d/1V8oaqxfu7A-rUq9BtZKxqP0Gy2WHFumiGNzh5piX9TI/edit#gid=1022075175
 */

class ProductReviewDetailTracking {

    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }

    private val EVENT = "event"
    private val EVENT_CATEGORY = "eventCategory"
    private val EVENT_ACTION = "eventAction"
    private val EVENT_LABEL = "eventLabel"
    private val EVENT_CLICK_REVIEW = "clickReview"
    private val CLICK = "click"

    private val FIELD_PRODUCT_DETAIL = "review detail page"
    private val FIELD_SCREEN_NAME = "screenName"
    private val FIELD_SHOP_ID = "shopId"
    private val FIELD_PRODUCT_ID = "productId"
    private val FIELD_FEEDBACK_ID = "feedbackId"
    private val FIELD_IS_ACTIVE = "isActive"

    fun sendScreenDetail(shopId: String, productId: String) {
        val dataLayer = mutableMapOf<String, String>()
        dataLayer[FIELD_SCREEN_NAME] = FIELD_PRODUCT_DETAIL
        dataLayer[FIELD_SHOP_ID] = shopId
        dataLayer[FIELD_PRODUCT_ID] = productId
        tracker.sendScreenAuthenticated(FIELD_SCREEN_NAME, dataLayer)
    }

    fun eventClickTimeFilter(shopId: String, productId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - time filter",
                EVENT_LABEL, "",
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    fun eventClickApplyTimeFilter(shopId: String, productId: String, filterSelected: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - apply time filter",
                EVENT_LABEL, "filterBy:$filterSelected",
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    fun eventClickStarFilter(shopId: String,
                             productId: String,
                             starSelected: String,
                             isActive: String) {

        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - star filter",
                EVENT_LABEL, "star:$starSelected",
                FIELD_IS_ACTIVE, isActive,
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    fun eventClickOptionMenuDetail(shopId: String, productId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - three dots on review detail page",
                EVENT_LABEL, "",
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    fun eventClickOptionEditProduct(shopId: String, productId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - three dots options on bottom sheet",
                EVENT_LABEL, "options:Ubah Produk",
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    fun eventClickFilterTopicSelected(shopId: String,
                                      productId: String,
                                      filterSelected: String,
                                      isActive: String) {

        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - quick filter on review detail page",
                EVENT_LABEL, "filterBy:$filterSelected",
                FIELD_IS_ACTIVE, isActive,
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    fun eventClickSortOrFilterTopics(shopId: String, productId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - sort or filter button",
                EVENT_LABEL, "",
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    fun eventClickSortOnBottomSheet(shopId: String, productId: String, sortSelected: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - select sort options on bottomsheet",
                EVENT_LABEL, "sortBy:$sortSelected",
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    fun eventClickFilterOnBottomSheet(shopId: String, productId: String, filterSelected: String, isActive: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - select filter options on bottomsheet",
                EVENT_LABEL, "filterBy:$filterSelected",
                FIELD_IS_ACTIVE, isActive,
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    fun eventClickResetOnBottomSheet(shopId: String, productId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - reset on sort or filter bottomsheet",
                EVENT_LABEL, "",
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    fun eventClickCloseBottomSheetSortFilter(shopId: String, productId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - close bottomsheet",
                EVENT_LABEL, "",
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    fun eventClickOptionFeedbackReview(shopId: String, productId: String, feedbackId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - three dots on product review",
                EVENT_LABEL, "feedbackId:$feedbackId",
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    fun eventClickReportOnBottomSheet(shopId: String, productId: String, feedbackId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - select three dots menu on product review",
                EVENT_LABEL, "options:Laporkan;$FIELD_FEEDBACK_ID:$feedbackId",
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    fun eventClickCloseFeedbackOptionBottomSheet(shopId: String, productId: String, feedbackId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - close on three dots on product review",
                EVENT_LABEL, "$FIELD_FEEDBACK_ID:$feedbackId",
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    fun eventClickReadMoreFeedback(shopId: String, productId: String, feedbackId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - selengkapnya on product review",
                EVENT_LABEL, "$FIELD_FEEDBACK_ID:$feedbackId",
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    fun eventSwipeBottomSheetSortFilterTopics(shopId: String, productId: String, state: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - drag $state bottomsheet",
                EVENT_LABEL, "",
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId,
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

    //attachmentId = imageUrl
    fun eventClickImagePreviewSlider(feedbackId: String, attachmentId: String, position: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_PRODUCT_DETAIL,
                EVENT_ACTION, "$CLICK - image on product review",
                EVENT_LABEL, "$FIELD_FEEDBACK_ID:$feedbackId;attachmentId:$attachmentId;position:$position",
                FIELD_SCREEN_NAME, FIELD_PRODUCT_DETAIL
        ))
    }

}