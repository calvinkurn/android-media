package com.tokopedia.review.feature.createreputation.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.track.TrackApp
import java.util.HashMap

object CreateReviewTracking {

    fun reviewOnAnonymousClickTracker(
            orderId: String,
            productId: String,
            isEditReview: Boolean
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                "clickReview",
                CreateReviewTrackingConstants.EVENT_CATEGORY + getEditMarker(isEditReview),
                "click - anonim on ulasan produk",
                "$orderId - $productId"
        ))
    }


    fun reviewOnCloseTracker(orderId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                "clickReview",
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                "click - back button on product review detail page",
                "$orderId - $productId"
        ))
    }

    fun reviewOnSubmitTracker(
            orderId: String,
            productId: String,
            ratingValue: String,
            isMessageEmpty: Boolean,
            imageNum: String,
            isAnonymous: Boolean,
            isEditReview: Boolean
    ) {
        val messageState = if (isMessageEmpty) "blank" else "filled"
        val anonymousState = if (isAnonymous) "true" else "false"
        TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                "clickReview",
                CreateReviewTrackingConstants.EVENT_CATEGORY + getEditMarker(isEditReview),
                "click - kirim ulasan produk",
                "order_id : " + orderId +
                        " - product_id : " + productId +
                        " - star : " + ratingValue +
                        " - ulasan : " + messageState +
                        " - gambar : " + imageNum +
                        " - anonim : " + anonymousState
        ))
    }

    fun reviewOnMessageChangedTracker(
            orderId: String,
            productId: String,
            isMessageEmpty: Boolean,
            isEditReview: Boolean
    ) {
        val messageState = if (isMessageEmpty) "blank" else "filled"
        TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                "clickReview",
                CreateReviewTrackingConstants.EVENT_CATEGORY + getEditMarker(isEditReview),
                "click - ulasan product description",
                "$orderId - $productId - $messageState"
        ))
    }

    fun reviewOnViewTracker(orderId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                "viewReviewIris",
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                "view - product review detail page",
                "$orderId - $productId"
        ))
    }

    fun reviewOnImageUploadTracker(
            orderId: String,
            productId: String,
            isSuccessful: Boolean,
            imageNum: String,
            isEditReview: Boolean
    ) {
        val successState = if (isSuccessful) "success" else "unsuccessful"
        TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                "clickReview",
                CreateReviewTrackingConstants.EVENT_CATEGORY + getEditMarker(isEditReview),
                "click - upload gambar produk",
                "$orderId - $productId - $successState - $imageNum"
        ))
    }

    fun reviewOnRatingChangedTracker(
            orderId: String,
            productId: String,
            ratingValue: String,
            isSuccessful: Boolean,
            isEditReview: Boolean
    ) {
        val successState = if (isSuccessful) "success" else "unsuccessful"
        TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page" + getEditMarker(isEditReview),
                "click - product star rating - $ratingValue",
                "$orderId - $productId - $successState"
        ))
    }

    fun onExpandTextBoxClicked(orderId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                CreateReviewTrackingConstants.CLICK_EXPAND_TEXTBOX,
                String.format(CreateReviewTrackingConstants.EVENT_LABEL_ORDER_ID_PRODUCT_ID, orderId, productId)
        ))
    }

    fun onCollapseTextBoxClicked(orderId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                CreateReviewTrackingConstants.CLICK_COLLAPSE_TEXT_BOX,
                String.format(CreateReviewTrackingConstants.EVENT_LABEL_ORDER_ID_PRODUCT_ID, orderId, productId)
        ))
    }

    fun eventClickSmiley(orderId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                ReviewTrackingConstant.CLICK_SMILEY,
                String.format(CreateReviewTrackingConstants.EVENT_LABEL_ORDER_ID_PRODUCT_ID, orderId, productId)
        ))
    }

    private fun createEventMap(event: String, category: String, action: String, label: String): HashMap<String, Any>? {
        val eventMap = HashMap<String, Any>()
        eventMap[ReviewTrackingConstant.EVENT] = event
        eventMap[ReviewTrackingConstant.EVENT_CATEGORY] = category
        eventMap[ReviewTrackingConstant.EVENT_ACTION] = action
        eventMap[ReviewTrackingConstant.EVENT_LABEL] = label
        return eventMap
    }

    private fun getEditMarker(isEditReview: Boolean): String? {
        return if (isEditReview) " - edit" else ""
    }
}