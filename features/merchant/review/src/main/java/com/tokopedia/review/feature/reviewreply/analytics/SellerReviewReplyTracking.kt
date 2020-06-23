package com.tokopedia.review.feature.reviewreply.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

/*
Review Seller Revamp
Data layer docs
https://docs.google.com/spreadsheets/d/1V8oaqxfu7A-rUq9BtZKxqP0Gy2WHFumiGNzh5piX9TI/edit#gid=1022075175
 */

class SellerReviewReplyTracking {

    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }

    private val EVENT = "event"
    private val EVENT_CATEGORY = "eventCategory"
    private val EVENT_ACTION = "eventAction"
    private val EVENT_LABEL = "eventLabel"
    private val EVENT_CLICK_REVIEW = "clickReview"
    private val CLICK = "click"

    private val FIELD_REVIEW_REPLY = "review reply page"

    private val FIELD_SCREEN_NAME = "screenName"
    private val FIELD_SHOP_ID = "shopId"
    private val FIELD_PRODUCT_ID = "productId"
    private val FIELD_FEEDBACK_ID = "feedbackId"
    private val FIELD_MESSAGE = "message"

    fun eventClickResponseReview(shopId: String, productId: String, feedbackId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_REVIEW_REPLY,
                EVENT_ACTION, "$CLICK - response review text field",
                EVENT_LABEL, "$FIELD_FEEDBACK_ID:$feedbackId",
                FIELD_SCREEN_NAME, FIELD_REVIEW_REPLY,
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId
        ))
    }

    fun eventClickAddTemplateReview(shopId: String, productId: String, feedbackId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_REVIEW_REPLY,
                EVENT_ACTION, "$CLICK - add template review response",
                EVENT_LABEL, "$FIELD_FEEDBACK_ID:$feedbackId",
                FIELD_SCREEN_NAME, FIELD_REVIEW_REPLY,
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId
        ))
    }

    fun eventClickItemReviewTemplate(shopId: String, productId: String, feedbackId: String, message: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_REVIEW_REPLY,
                EVENT_ACTION, "$CLICK - saved template review response",
                EVENT_LABEL, "$FIELD_MESSAGE:$message;$FIELD_FEEDBACK_ID:$feedbackId",
                FIELD_SCREEN_NAME, FIELD_REVIEW_REPLY,
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId
        ))
    }

    fun eventClickSendReviewReply(shopId: String, productId: String, feedbackId: String, message: String, isEdit: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_REVIEW_REPLY,
                EVENT_ACTION, "$CLICK - kirim button to reply review",
                EVENT_LABEL, "$FIELD_MESSAGE:$message;$FIELD_FEEDBACK_ID:$feedbackId;is_edit:$isEdit",
                FIELD_SCREEN_NAME, FIELD_REVIEW_REPLY,
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId
        ))
    }

    fun eventClickThreeDotsMenu(shopId: String, productId: String, feedbackId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_REVIEW_REPLY,
                EVENT_ACTION, "$CLICK - three dots",
                EVENT_LABEL, "$FIELD_FEEDBACK_ID:$feedbackId",
                FIELD_SCREEN_NAME, FIELD_REVIEW_REPLY,
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId
        ))
    }

    fun eventClickItemReportOnBottomSheet(shopId: String, productId: String, feedbackId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_REVIEW_REPLY,
                EVENT_ACTION, "$CLICK - laporkan review from three dots menu",
                EVENT_LABEL, "$FIELD_FEEDBACK_ID:$feedbackId",
                FIELD_SCREEN_NAME, FIELD_REVIEW_REPLY,
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId
        ))
    }

    fun eventClickEditReviewResponse(shopId: String, productId: String, feedbackId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_REVIEW_REPLY,
                EVENT_ACTION, "$CLICK - ubah on review response",
                EVENT_LABEL, "$FIELD_FEEDBACK_ID:$feedbackId",
                FIELD_SCREEN_NAME, FIELD_REVIEW_REPLY,
                FIELD_SHOP_ID, shopId,
                FIELD_PRODUCT_ID, productId
        ))
    }


}