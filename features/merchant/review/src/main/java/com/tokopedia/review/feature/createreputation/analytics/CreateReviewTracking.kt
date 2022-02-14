package com.tokopedia.review.feature.createreputation.analytics

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewDialogType
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object CreateReviewTracking {

    fun reviewOnAnonymousClickTracker(
        orderId: String,
        productId: String,
        isEditReview: Boolean,
        feedbackId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                "clickReview",
                CreateReviewTrackingConstants.EVENT_CATEGORY + getEditMarker(isEditReview),
                "click - anonim on ulasan produk",
                "${if (isEditReview) feedbackId else orderId} - $productId"
            )
        )
    }


    fun reviewOnCloseTracker(orderId: String, productId: String, isEligible: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                "clickReview",
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                "click - back button on product review detail page",
                "$orderId - $productId - product_is_incentive_eligible: $isEligible;"
            )
        )
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
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                "clickReview",
                CreateReviewTrackingConstants.EVENT_CATEGORY + getEditMarker(isEditReview),
                "click - kirim ulasan produk",
                "order_id : " + if (isEditReview) feedbackId else orderId +
                        " - product_id : " + productId +
                        " - star : " + ratingValue +
                        " - ulasan : " + messageState +
                        " - gambar : " + imageNum +
                        " - anonim : " + anonymousState +
                        " - feedback is incentive eligible : " + isEligible
            )
        )
    }

    fun reviewOnMessageChangedTracker(
        orderId: String,
        productId: String,
        isMessageEmpty: Boolean,
        isEditReview: Boolean,
        feedbackId: String
    ) {
        val messageState = if (isMessageEmpty) "blank" else "filled"
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                "clickReview",
                CreateReviewTrackingConstants.EVENT_CATEGORY + getEditMarker(isEditReview),
                "click - ulasan product description",
                "${if (isEditReview) feedbackId else orderId} - $productId - $messageState"
            )
        )
    }

    fun reviewOnScoreVisible(orderId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                CreateReviewTrackingConstants.EVENT_ACTION_VIEW_SMILEY,
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_ORDER_ID_PRODUCT_ID,
                    orderId,
                    productId
                )
            )
        )
    }

    fun reviewOnViewTracker(orderId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                "viewReviewIris",
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                "view - product review detail page",
                "$orderId - $productId"
            )
        )
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
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                "clickReview",
                CreateReviewTrackingConstants.EVENT_CATEGORY + getEditMarker(isEditReview),
                "click - upload gambar produk",
                "${if (isEditReview) orderId else feedbackId} - $productId - $successState - $imageNum"
            )
        )
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
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                "clickReview",
                "product review detail page" + getEditMarker(isEditReview),
                "click - product star rating - $ratingValue",
                "${if (isEditReview) feedbackId else orderId} - $productId - $successState"
            )
        )
    }

    fun onExpandTextBoxClicked(orderId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                CreateReviewTrackingConstants.EVENT_ACTION_CLICK_EXPAND_TEXTBOX,
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_ORDER_ID_PRODUCT_ID,
                    orderId,
                    productId
                )
            )
        )
    }

    fun onCollapseTextBoxClicked(orderId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                CreateReviewTrackingConstants.EVENT_ACTION_CLICK_COLLAPSE_TEXT_BOX,
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_ORDER_ID_PRODUCT_ID,
                    orderId,
                    productId
                )
            )
        )
    }

    fun eventClickSmiley(orderId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                ReviewTrackingConstant.CLICK_SMILEY,
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_ORDER_ID_PRODUCT_ID,
                    orderId,
                    productId
                )
            )
        )
    }

    fun eventViewDialog(dialogTitle: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.VIEW_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(CreateReviewTrackingConstants.EVENT_ACTION_VIEW_DIALOG, dialogTitle),
                CreateReviewTrackingConstants.EMPTY_LABEL
            )
        )
    }

    fun eventClickContinueWrite(dialogTitle: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(
                    CreateReviewTrackingConstants.EVENT_ACTION_CLICK_CONTINUE_IN_DIALOG,
                    dialogTitle
                ),
                CreateReviewTrackingConstants.EMPTY_LABEL
            )
        )
    }

    fun eventClickLeavePage(dialogTitle: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(
                    CreateReviewTrackingConstants.EVENT_ACTION_CLICK_LEAVE_PAGE,
                    dialogTitle
                ),
                CreateReviewTrackingConstants.EMPTY_LABEL
            )
        )
    }

    fun eventClickSendAnother(title: String, hasPendingIncentive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(CreateReviewTrackingConstants.EVENT_ACTION_CLICK_SEND_ANOTHER, title),
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_PENDING_INCENTIVE_QUEUE,
                    hasPendingIncentive.toString()
                )
            )
        )
    }

    fun eventClickLater(title: String, hasPendingIncentive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(CreateReviewTrackingConstants.EVENT_ACTION_CLICK_LATER, title),
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_PENDING_INCENTIVE_QUEUE,
                    hasPendingIncentive.toString()
                )
            )
        )
    }

    fun eventClickOk(title: String, hasPendingIncentive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(CreateReviewTrackingConstants.EVENT_ACTION_CLICK_OK, title),
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_PENDING_INCENTIVE_QUEUE,
                    hasPendingIncentive.toString()
                )
            )
        )
    }

    fun eventClickCompleteReviewFirst(dialogTitle: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(
                    CreateReviewTrackingConstants.EVENT_ACTION_CLICK_COMPLETE_REVIEW_FIRST,
                    dialogTitle
                ),
                CreateReviewTrackingConstants.EMPTY_LABEL
            )
        )
    }

    fun eventClickSendNow(dialogTitle: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY,
                String.format(
                    CreateReviewTrackingConstants.EVENT_ACTION_CLICK_SEND_NOW,
                    dialogTitle
                ),
                CreateReviewTrackingConstants.EMPTY_LABEL
            )
        )
    }

    fun openScreen(
        screenName: String,
        productId: String,
        reputationId: String,
        source: String
    ) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
            screenName, mapOf(
                CreateReviewTrackingConstants.KEY_DEEPLINK to getWriteFormDeeplinkAsString(
                    reputationId,
                    productId,
                    source
                )
            )
        )
    }

    fun openScreenWithCustomDimens(
        screenName: String,
        productId: String,
        reputationId: String,
        source: String
    ) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
            screenName,
            getOpenScreenEventMap(productId, reputationId, source)
        )
    }

    fun eventViewIncentivesTicker(
        message: String,
        reputationId: String,
        orderId: String,
        productId: String,
        userId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.VIEW_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                ReviewTrackingConstant.VIEW_OVO_INCENTIVES_TICKER,
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_VIEW_INCENTIVES_TICKER,
                    message,
                    reputationId,
                    orderId,
                    productId
                ),
                productId,
                userId
            )
        )
    }

    fun eventClickIncentivesTicker(
        message: String,
        reputationId: String,
        orderId: String,
        productId: String,
        userId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                CreateReviewTrackingConstants.EVENT_ACTION_CLICK_INCENTIVES_TICKER,
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_VIEW_INCENTIVES_TICKER,
                    message,
                    reputationId,
                    orderId,
                    productId
                ),
                productId,
                userId
            )
        )
    }

    fun eventViewDialog(
        dialogType: CreateReviewDialogType,
        title: String,
        reputationId: String,
        orderId: String,
        productId: String,
        userId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.VIEW_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                mapDialogTypeToViewDialogEventAction(dialogType),
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_VIEW_DIALOG,
                    title,
                    reputationId,
                    orderId,
                    productId
                ),
                productId,
                userId
            )
        )
    }

    fun eventClickDialogOption(
        dialogType: CreateReviewDialogType,
        title: String,
        reputationId: String,
        orderId: String,
        productId: String,
        userId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                mapDialogTypeToClickDialogEventAction(dialogType),
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_VIEW_DIALOG,
                    title,
                    reputationId,
                    orderId,
                    productId
                ),
                productId,
                userId
            )
        )
    }

    fun eventClickReviewTemplate(
        templateText: String,
        reputationId: String,
        orderId: String,
        productId: String,
        userId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                CreateReviewTrackingConstants.EVENT_ACTION_CLICK_TEMPLATE,
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_CLICK_TEMPLATE,
                    templateText,
                    reputationId,
                    orderId,
                    productId
                ),
                productId,
                userId
            )
        )
    }

    fun eventViewReviewTemplate(templateCount: Int, productId: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.VIEW_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                CreateReviewTrackingConstants.EVENT_ACTION_VIEW_TEMPLATE,
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_VIEW_TEMPLATE,
                    templateCount
                ),
                productId,
                userId
            )
        )
    }

    fun eventClickSubmitForm(
        rating: Int,
        textLength: Int,
        numberOfPicture: Int,
        isAnonymous: Boolean,
        isEligible: Boolean,
        isTemplateAvailable: Boolean,
        templateSelectedCount: Int,
        orderId: String,
        productId: String,
        userId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                CreateReviewTrackingConstants.EVENT_ACTION_CLICK_SUBMIT,
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_CLICK_SUBMIT,
                    orderId,
                    productId,
                    rating,
                    textLength > 0,
                    textLength,
                    numberOfPicture,
                    isAnonymous.toString(),
                    isEligible.toString(),
                    isTemplateAvailable.toString(),
                    templateSelectedCount
                ),
                productId,
                userId
            )
        )
    }

    fun eventDismissForm(
        rating: Int,
        textLength: Int,
        numberOfPicture: Int,
        isAnonymous: Boolean,
        isEligible: Boolean,
        isTemplateAvailable: Boolean,
        templateSelectedCount: Int,
        orderId: String,
        productId: String,
        userId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                CreateReviewTrackingConstants.EVENT_ACTION_DISMISS_FORM,
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_CLICK_SUBMIT,
                    orderId,
                    productId,
                    rating,
                    textLength > 0,
                    textLength,
                    numberOfPicture,
                    isAnonymous.toString(),
                    isEligible.toString(),
                    isTemplateAvailable.toString(),
                    templateSelectedCount
                ),
                productId,
                userId
            )
        )
    }

    fun eventViewThankYouBottomSheet(
        title: String,
        reputationId: String,
        orderId: String,
        feedbackId: String,
        productId: String,
        userId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.VIEW_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                CreateReviewTrackingConstants.EVENT_ACTION_VIEW_THANK_YOU_BOTTOM_SHEET,
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_VIEW_THANK_YOU_BOTTOM_SHEET,
                    title,
                    reputationId,
                    orderId,
                    productId,
                    feedbackId
                ),
                productId,
                userId
            )
        )
    }

    fun eventViewBadRatingReason(
        trackingQueue: TrackingQueue,
        orderId: String,
        productId: String,
        badRatingReason: String,
        userId: String
    ) {
        trackingQueue.putEETracking(
            hashMapOf(
                ReviewTrackingConstant.EVENT to CreateReviewTrackingConstants.EVENT_PROMO_VIEW,
                ReviewTrackingConstant.EVENT_ACTION to CreateReviewTrackingConstants.EVENT_ACTION_IMPRESS_BAD_RATING_REASON,
                ReviewTrackingConstant.EVENT_LABEL to String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_IMPRESS_BAD_RATING_REASON,
                    orderId,
                    productId
                ),
                ReviewTrackingConstant.EVENT_CATEGORY to CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                CreateReviewTrackingConstants.KEY_PRODUCT_ID to productId,
                ReviewTrackingConstant.KEY_USER_ID to userId,
                CreateReviewTrackingConstants.KEY_BUSINESS_UNIT to CreateReviewTrackingConstants.BUSINESS_UNIT,
                CreateReviewTrackingConstants.KEY_CURRENT_SITE to CreateReviewTrackingConstants.CURRENT_SITE,
                CreateReviewTrackingConstants.KEY_ECOMMERCE to mapOf(
                    CreateReviewTrackingConstants.EVENT_PROMO_VIEW to mapOf(
                        CreateReviewTrackingConstants.KEY_PROMOTIONS to listOf(
                            mapOf(
                                CreateReviewTrackingConstants.KEY_ID to String.format(
                                    CreateReviewTrackingConstants.BAD_RATING_REASON_IMPRESSION_ENHANCED_ECOMMERCE_ID,
                                    badRatingReason
                                ),
                                CreateReviewTrackingConstants.KEY_CREATIVE to null.toString(),
                                CreateReviewTrackingConstants.KEY_NAME to null.toString(),
                                CreateReviewTrackingConstants.KEY_POSITION to null.toString(),
                            )
                        )
                    )
                )
            )
        )
    }

    fun eventClickBadRatingReason(
        orderId: String,
        productId: String,
        userId: String,
        badRatingReason: String,
        isActive: Boolean
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                CreateReviewTrackingConstants.EVENT_ACTION_CLICK_BAD_RATING_REASON,
                String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_CLICK_BAD_RATING_REASON,
                    orderId,
                    productId,
                    badRatingReason,
                    isActive.toString()
                ),
                productId,
                userId
            )
        )
    }

    private fun createEventMap(
        event: String,
        category: String,
        action: String,
        label: String
    ): HashMap<String, Any> {
        val eventMap = HashMap<String, Any>()
        eventMap[ReviewTrackingConstant.EVENT] = event
        eventMap[ReviewTrackingConstant.EVENT_CATEGORY] = category
        eventMap[ReviewTrackingConstant.EVENT_ACTION] = action
        eventMap[ReviewTrackingConstant.EVENT_LABEL] = label
        return eventMap
    }

    private fun createEventMap(
        event: String,
        category: String,
        action: String,
        label: String,
        productId: String,
        userId: String
    ): HashMap<String, Any> {
        val eventMap = HashMap<String, Any>()
        eventMap[ReviewTrackingConstant.EVENT] = event
        eventMap[ReviewTrackingConstant.EVENT_CATEGORY] = category
        eventMap[ReviewTrackingConstant.EVENT_ACTION] = action
        eventMap[ReviewTrackingConstant.EVENT_LABEL] = label
        eventMap[ReviewTrackingConstant.KEY_USER_ID] = userId
        eventMap[CreateReviewTrackingConstants.KEY_BUSINESS_UNIT] =
            CreateReviewTrackingConstants.BUSINESS_UNIT
        eventMap[CreateReviewTrackingConstants.KEY_CURRENT_SITE] =
            CreateReviewTrackingConstants.CURRENT_SITE
        eventMap[CreateReviewTrackingConstants.KEY_PRODUCT_ID] = productId
        return eventMap
    }

    private fun getEditMarker(isEditReview: Boolean): String {
        return if (isEditReview) " - edit" else ""
    }

    private fun mapDialogTypeToViewDialogEventAction(dialogType: CreateReviewDialogType): String {
        return when (dialogType) {
            CreateReviewDialogType.CreateReviewSendRatingOnlyDialog -> CreateReviewTrackingConstants.EVENT_ACTION_VIEW_SEND_RATING_DIALOG
            CreateReviewDialogType.CreateReviewUnsavedDialog -> CreateReviewTrackingConstants.EVENT_ACTION_VIEW_UNSAVED_DIALOG
        }
    }

    private fun mapDialogTypeToClickDialogEventAction(dialogType: CreateReviewDialogType): String {
        return when (dialogType) {
            CreateReviewDialogType.CreateReviewSendRatingOnlyDialog -> CreateReviewTrackingConstants.EVENT_ACTION_CLICK_SEND_RATING_OPTION
            CreateReviewDialogType.CreateReviewUnsavedDialog -> CreateReviewTrackingConstants.EVENT_ACTION_CLICK_STAY_OPTION
        }
    }

    private fun getOpenScreenEventMap(
        productId: String,
        reputationId: String,
        source: String
    ): Map<String, String> {
        return mapOf(
            CreateReviewTrackingConstants.KEY_BUSINESS_UNIT to CreateReviewTrackingConstants.BUSINESS_UNIT,
            CreateReviewTrackingConstants.KEY_CURRENT_SITE to CreateReviewTrackingConstants.CURRENT_SITE,
            CreateReviewTrackingConstants.KEY_PRODUCT_ID to productId,
            CreateReviewTrackingConstants.KEY_DEEPLINK to getWriteFormDeeplinkAsString(
                reputationId,
                productId,
                source
            )
        )
    }

    private fun getWriteFormDeeplinkAsString(
        reputationId: String,
        productId: String,
        source: String
    ): String {
        return Uri.parse(
            UriUtil.buildUri(
                ApplinkConst.PRODUCT_CREATE_REVIEW,
                reputationId,
                productId
            )
        )
            .buildUpon()
            .appendQueryParameter(ReviewConstants.PARAM_SOURCE, source)
            .build()
            .toString()
    }
}