package com.tokopedia.review.common.analytics

import com.tokopedia.track.TrackApp
import java.util.*

object ReviewTracking {

    fun onSuccessGetIncentiveOvoTracker(message: String?, category: String) {
        if (category.isEmpty()) {
            TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.VIEW_REVIEW,
                    ReviewTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReviewTrackingConstant.VIEW_OVO_INCENTIVES_TICKER,
                    ReviewTrackingConstant.MESSAGE + message + ";"
            ))
        } else {
            TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.VIEW_REVIEW,
                    ReviewTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReviewTrackingConstant.VIEW_OVO_INCENTIVES_TICKER,
                    ReviewTrackingConstant.MESSAGE + message + ";"
            ))
        }
    }

    fun onClickReadSkIncentiveOvoTracker(message: String?, category: String) {
        if (category.isEmpty()) {
            TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReviewTrackingConstant.CLICK_READ_SK_OVO_INCENTIVES_TICKER,
                    ReviewTrackingConstant.MESSAGE + message + ";"
            ))
        } else {
            TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReviewTrackingConstant.CLICK_READ_SK_OVO_INCENTIVES_TICKER,
                    ReviewTrackingConstant.MESSAGE + message + ";"
            ))
        }
    }

    fun onClickDismissIncentiveOvoTracker(message: String?, category: String) {
        if (category.isEmpty()) {
            TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReviewTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_TICKER,
                    ReviewTrackingConstant.MESSAGE + message + ";"
            ))
        } else {
            TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReviewTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_TICKER,
                    ReviewTrackingConstant.MESSAGE + message + ";"
            ))
        }
    }

    fun onClickDismissIncentiveOvoBottomSheetTracker(category: String) {
        if (category.isEmpty()) {
            TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReviewTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_BOTTOMSHEET,
                    ""
            ))
        } else {
            TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReviewTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_BOTTOMSHEET,
                    ""
            ))
        }
    }

    fun onClickContinueIncentiveOvoBottomSheetTracker(category: String) {
        if (category.isEmpty()) {
            TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReviewTrackingConstant.CLICK_CONTINUE_SEND_REVIEW_0N_OVO_INCENTIVES,
                    ""
            ))
        } else {
            TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReviewTrackingConstant.CLICK_CONTINUE_SEND_REVIEW_0N_OVO_INCENTIVES,
                    ""
            ))
        }
    }

    fun reviewOnAnonymousClickTracker(
            orderId: String,
            productId: String,
            isEditReview: Boolean
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page" + getEditMarker(isEditReview),
                "click - anonim on ulasan produk",
                "$orderId - $productId"
        ))
    }


    fun reviewOnCloseTracker(orderId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page",
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
                "product review detail page" + getEditMarker(isEditReview),
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
                "product review detail page" + getEditMarker(isEditReview),
                "click - ulasan product description",
                "$orderId - $productId - $messageState"
        ))
    }

    fun reviewOnViewTracker(orderId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                "viewReviewIris",
                "product review detail page",
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
                "product review detail page" + getEditMarker(isEditReview),
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