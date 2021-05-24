package com.tokopedia.review.feature.createreputation.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewDialogType
import com.tokopedia.track.TrackApp
import java.util.*

object CreateReviewTracking {

    private val tracker = TrackApp.getInstance().gtm

    fun reviewOnAnonymousClickTracker(
            orderId: String,
            productId: String,
            isEditReview: Boolean,
            feedbackId: String
    ) {
        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                CreateReviewTrackingConstants.EVENT_CATEGORY + getEditMarker(isEditReview),
                "click - anonim on ulasan produk",
                "${if(isEditReview) feedbackId else orderId} - $productId"
        ))
    }


    fun reviewOnCloseTracker(orderId: String, productId: String, isEligible: Boolean) {
        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                "click - back button on product review detail page",
                "$orderId - $productId - product_is_incentive_eligible: $isEligible;"
        ))
    }

    fun reviewOnSubmitTracker(
            orderId: String,
            productId: String,
            ratingValue: String,
            isMessageEmpty: Boolean,
            imageNum: String,
            isAnonymous: Boolean,
            isEditReview: Boolean,
            feedbackId: String,
            isEligible: Boolean
    ) {
        val messageState = if (isMessageEmpty) "blank" else "filled"
        val anonymousState = if (isAnonymous) "true" else "false"
        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                CreateReviewTrackingConstants.EVENT_CATEGORY + getEditMarker(isEditReview),
                "click - kirim ulasan produk",
                "order_id : " + if(isEditReview) feedbackId else orderId +
                        " - product_id : " + productId +
                        " - star : " + ratingValue +
                        " - ulasan : " + messageState +
                        " - gambar : " + imageNum +
                        " - anonim : " + anonymousState +
                        " - feedback is incentive eligible : " + isEligible
        ))
    }

    fun reviewOnMessageChangedTracker(
            orderId: String,
            productId: String,
            isMessageEmpty: Boolean,
            isEditReview: Boolean,
            feedbackId: String
    ) {
        val messageState = if (isMessageEmpty) "blank" else "filled"
        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                CreateReviewTrackingConstants.EVENT_CATEGORY + getEditMarker(isEditReview),
                "click - ulasan product description",
                "${if(isEditReview) feedbackId else orderId} - $productId - $messageState"
        ))
    }

    fun reviewOnScoreVisible(orderId: String, productId: String) {
        tracker.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                CreateReviewTrackingConstants.VIEW_SMILEY,
                String.format(CreateReviewTrackingConstants.EVENT_LABEL_ORDER_ID_PRODUCT_ID, orderId, productId)
        ))
    }

    fun reviewOnViewTracker(orderId: String, productId: String) {
        tracker.sendGeneralEvent(createEventMap(
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
            isEditReview: Boolean,
            feedbackId: String
    ) {
        val successState = if (isSuccessful) "success" else "unsuccessful"
        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                CreateReviewTrackingConstants.EVENT_CATEGORY + getEditMarker(isEditReview),
                "click - upload gambar produk",
                "${if(isEditReview) orderId else feedbackId} - $productId - $successState - $imageNum"
        ))
    }

    fun reviewOnRatingChangedTracker(
            orderId: String,
            productId: String,
            ratingValue: String,
            isSuccessful: Boolean,
            isEditReview: Boolean,
            feedbackId: String
    ) {
        val successState = if (isSuccessful) "success" else "unsuccessful"
        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page" + getEditMarker(isEditReview),
                "click - product star rating - $ratingValue",
                "${if(isEditReview) feedbackId else orderId} - $productId - $successState"
        ))
    }

    fun onExpandTextBoxClicked(orderId: String, productId: String) {
        tracker.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                CreateReviewTrackingConstants.CLICK_EXPAND_TEXTBOX,
                String.format(CreateReviewTrackingConstants.EVENT_LABEL_ORDER_ID_PRODUCT_ID, orderId, productId)
        ))
    }

    fun onCollapseTextBoxClicked(orderId: String, productId: String) {
        tracker.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                CreateReviewTrackingConstants.CLICK_COLLAPSE_TEXT_BOX,
                String.format(CreateReviewTrackingConstants.EVENT_LABEL_ORDER_ID_PRODUCT_ID, orderId, productId)
        ))
    }

    fun eventClickSmiley(orderId: String, productId: String) {
        tracker.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                ReviewTrackingConstant.CLICK_SMILEY,
                String.format(CreateReviewTrackingConstants.EVENT_LABEL_ORDER_ID_PRODUCT_ID, orderId, productId)
        ))
    }

    fun eventViewDialog(dialogTitle: String) {
        tracker.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.VIEW_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(CreateReviewTrackingConstants.VIEW_DIALOG, dialogTitle),
                CreateReviewTrackingConstants.EMPTY_LABEL
        ))
    }

    fun eventClickContinueWrite(dialogTitle: String) {
        tracker.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(CreateReviewTrackingConstants.CLICK_CONTINUE_IN_DIALOG, dialogTitle),
                CreateReviewTrackingConstants.EMPTY_LABEL
        ))
    }

    fun eventClickLeavePage(dialogTitle: String) {
        tracker.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(CreateReviewTrackingConstants.CLICK_LEAVE_PAGE, dialogTitle),
                CreateReviewTrackingConstants.EMPTY_LABEL
        ))
    }

    fun eventViewThankYouBottomSheet(bottomSheetTitle: String, hasPendingIncentive: Boolean) {
        tracker.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.VIEW_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(CreateReviewTrackingConstants.VIEW_DIALOG, bottomSheetTitle),
                String.format(CreateReviewTrackingConstants.EVENT_LABEL_PENDING_INCENTIVE_QUEUE, hasPendingIncentive.toString())
        ))
    }

    fun eventClickSendAnother(title: String, hasPendingIncentive: Boolean) {
        tracker.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(CreateReviewTrackingConstants.CLICK_SEND_ANOTHER, title),
                String.format(CreateReviewTrackingConstants.EVENT_LABEL_PENDING_INCENTIVE_QUEUE, hasPendingIncentive.toString())
        ))
    }

    fun eventClickLater(title: String, hasPendingIncentive: Boolean) {
        tracker.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(CreateReviewTrackingConstants.CLICK_LATER, title),
                String.format(CreateReviewTrackingConstants.EVENT_LABEL_PENDING_INCENTIVE_QUEUE, hasPendingIncentive.toString())
        ))
    }

    fun eventClickOk(title: String, hasPendingIncentive: Boolean) {
        tracker.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(CreateReviewTrackingConstants.CLICK_OK, title),
                String.format(CreateReviewTrackingConstants.EVENT_LABEL_PENDING_INCENTIVE_QUEUE, hasPendingIncentive.toString())
        ))
    }

    fun eventClickCompleteReviewFirst(dialogTitle: String) {
        tracker.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(CreateReviewTrackingConstants.CLICK_COMPLETE_REVIEW_FIRST, dialogTitle),
                CreateReviewTrackingConstants.EMPTY_LABEL
        ))
    }

    fun eventClickSendNow(dialogTitle: String) {
        tracker.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(CreateReviewTrackingConstants.CLICK_SEND_NOW, dialogTitle),
                CreateReviewTrackingConstants.EMPTY_LABEL
        ))
    }

    fun openScreen(screenName: String) {
        tracker.sendScreenAuthenticated(screenName)
    }

    fun eventViewDialog(dialogType: CreateReviewDialogType, title: String, reputationId: String, orderId: String, productId: String, userId: String) {
        tracker.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.VIEW_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                mapDialogTypeToViewDialogEventAction(dialogType),
                String.format(CreateReviewTrackingConstants.EVENT_LABEL_VIEW_DIALOG, title, reputationId, orderId, productId),
                productId,
                userId
        ))
    }

    fun eventClickDialogOption(dialogType: CreateReviewDialogType, title: String, reputationId: String, orderId: String, productId: String, userId: String) {
        tracker.sendGeneralEvent(createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                mapDialogTypeToClickDialogEventAction(dialogType),
                String.format(CreateReviewTrackingConstants.EVENT_LABEL_VIEW_DIALOG, title, reputationId, orderId, productId),
                productId,
                userId
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

    private fun createEventMap(event: String, category: String, action: String, label: String, productId: String, userId: String): HashMap<String, Any>? {
        val eventMap = HashMap<String, Any>()
        eventMap[ReviewTrackingConstant.EVENT] = event
        eventMap[ReviewTrackingConstant.EVENT_CATEGORY] = category
        eventMap[ReviewTrackingConstant.EVENT_ACTION] = action
        eventMap[ReviewTrackingConstant.EVENT_LABEL] = label
        eventMap[ReviewTrackingConstant.KEY_USER_ID] = userId
        eventMap[CreateReviewTrackingConstants.KEY_BUSINESS_UNIT] = CreateReviewTrackingConstants.BUSINESS_UNIT
        eventMap[CreateReviewTrackingConstants.KEY_CURRENT_SITE] = CreateReviewTrackingConstants.CURRENT_SITE
        eventMap[CreateReviewTrackingConstants.KEY_PRODUCT_ID] = productId
        return eventMap
    }

    private fun getEditMarker(isEditReview: Boolean): String? {
        return if (isEditReview) " - edit" else ""
    }

    private fun mapDialogTypeToViewDialogEventAction(dialogType: CreateReviewDialogType): String {
        return when(dialogType) {
            CreateReviewDialogType.CreateReviewSendRatingOnlyDialog -> CreateReviewTrackingConstants.EVENT_ACTION_VIEW_SEND_RATING_DIALOG
            CreateReviewDialogType.CreateReviewUnsavedDialog -> CreateReviewTrackingConstants.EVENT_ACTION_VIEW_UNSAVED_DIALOG
        }
    }

    private fun mapDialogTypeToClickDialogEventAction(dialogType: CreateReviewDialogType): String {
        return when(dialogType) {
            CreateReviewDialogType.CreateReviewSendRatingOnlyDialog -> CreateReviewTrackingConstants.EVENT_ACTION_CLICK_SEND_RATING_OPTION
            CreateReviewDialogType.CreateReviewUnsavedDialog -> CreateReviewTrackingConstants.EVENT_ACTION_CLICK_STAY_OPTION
        }
    }
}