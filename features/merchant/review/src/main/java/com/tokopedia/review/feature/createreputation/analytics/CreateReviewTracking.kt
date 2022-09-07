package com.tokopedia.review.feature.createreputation.analytics

import android.net.Uri
import android.os.Bundle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.picker.common.PageSource
import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewDialogType
import com.tokopedia.reviewcommon.constant.AnalyticConstant
import com.tokopedia.reviewcommon.extension.appendBusinessUnit
import com.tokopedia.reviewcommon.extension.appendCurrentSite
import com.tokopedia.reviewcommon.extension.appendGeneralEventData
import com.tokopedia.reviewcommon.extension.appendProductId
import com.tokopedia.reviewcommon.extension.appendUserId
import com.tokopedia.reviewcommon.extension.sendEnhancedEcommerce
import com.tokopedia.reviewcommon.extension.sendGeneralEvent
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
                event = "clickReview",
                category = CreateReviewTrackingConstants.EVENT_CATEGORY,
                action = "click - back button on product review detail page",
                label = "$orderId - $productId - product_is_incentive_eligible: $isEligible;"
            )
        )
    }

    fun reviewOnSubmitTracker(
        productId: String,
        ratingValue: String,
        isMessageEmpty: Boolean,
        imageNum: String,
        isAnonymous: Boolean,
        feedbackId: String,
        isEligible: Boolean
    ) {
        val messageState = if (isMessageEmpty) "blank" else "filled"
        val anonymousState = if (isAnonymous) "true" else "false"
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                event = "clickReview",
                category = CreateReviewTrackingConstants.EVENT_CATEGORY_EDIT,
                action = "click - kirim ulasan produk",
                label = "order_id : " + feedbackId +
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
        productId: String,
        isMessageEmpty: Boolean,
        feedbackId: String
    ) {
        val messageState = if (isMessageEmpty) "blank" else "filled"
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                event = "clickReview",
                category = CreateReviewTrackingConstants.EVENT_CATEGORY_EDIT,
                action = "click - ulasan product description",
                label = "$feedbackId - $productId - $messageState"
            )
        )
    }

    fun reviewOnScoreVisible(orderId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                event = ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY,
                action = CreateReviewTrackingConstants.EVENT_ACTION_VIEW_SMILEY,
                label = String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_ORDER_ID_PRODUCT_ID,
                    orderId,
                    productId
                )
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
                event = "clickReview",
                category = CreateReviewTrackingConstants.EVENT_CATEGORY + getEditMarker(isEditReview),
                action = "click - upload gambar produk",
                label = "${if (isEditReview) orderId else feedbackId} - $productId - $successState - $imageNum"
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
                event = "clickReview",
                category = "product review detail page" + getEditMarker(isEditReview),
                action = "click - product star rating - $ratingValue",
                label = "${if (isEditReview) feedbackId else orderId} - $productId - $successState"
            )
        )
    }

    fun onExpandTextBoxClicked(orderId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                event = ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY,
                action = CreateReviewTrackingConstants.EVENT_ACTION_CLICK_EXPAND_TEXTBOX,
                label = String.format(
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
                event = ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY,
                action = CreateReviewTrackingConstants.EVENT_ACTION_CLICK_COLLAPSE_TEXT_BOX,
                label = String.format(
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
                event = ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY,
                action = ReviewTrackingConstant.CLICK_SMILEY,
                label = String.format(
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
                event = ReviewTrackingConstant.VIEW_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY,
                action = String.format(CreateReviewTrackingConstants.EVENT_ACTION_VIEW_DIALOG, dialogTitle),
                label = CreateReviewTrackingConstants.EMPTY_LABEL
            )
        )
    }

    fun eventClickContinueWrite(dialogTitle: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                event = ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY,
                action = String.format(
                    CreateReviewTrackingConstants.EVENT_ACTION_CLICK_CONTINUE_IN_DIALOG,
                    dialogTitle
                ),
                label = CreateReviewTrackingConstants.EMPTY_LABEL
            )
        )
    }

    fun eventClickLeavePage(dialogTitle: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                event = ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY,
                action = String.format(
                    CreateReviewTrackingConstants.EVENT_ACTION_CLICK_LEAVE_PAGE,
                    dialogTitle
                ),
                label = CreateReviewTrackingConstants.EMPTY_LABEL
            )
        )
    }

    fun eventClickCompleteReviewFirst(dialogTitle: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                event = ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY,
                action = String.format(
                    CreateReviewTrackingConstants.EVENT_ACTION_CLICK_COMPLETE_REVIEW_FIRST,
                    dialogTitle
                ),
                label = CreateReviewTrackingConstants.EMPTY_LABEL
            )
        )
    }

    fun eventClickSendNow(dialogTitle: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                event = ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY,
                action = String.format(
                    CreateReviewTrackingConstants.EVENT_ACTION_CLICK_SEND_NOW,
                    dialogTitle
                ),
                label = CreateReviewTrackingConstants.EMPTY_LABEL
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
                event = ReviewTrackingConstant.VIEW_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                action = ReviewTrackingConstant.VIEW_OVO_INCENTIVES_TICKER,
                label = String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_VIEW_INCENTIVES_TICKER,
                    message,
                    reputationId,
                    orderId,
                    productId
                ),
                productId = productId,
                userId = userId
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
                event = ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                action = CreateReviewTrackingConstants.EVENT_ACTION_CLICK_INCENTIVES_TICKER,
                label = String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_VIEW_INCENTIVES_TICKER,
                    message,
                    reputationId,
                    orderId,
                    productId
                ),
                productId = productId,
                userId = userId
            )
        )
    }

    fun eventClickOngoingChallengeTicker(
        reputationId: String,
        orderId: String,
        productId: String,
        userId: String
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = ReviewTrackingConstant.EVENT_CLICK_REVIEW,
            eventCategory = CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
            eventAction = CreateReviewTrackingConstants.EVENT_ACTION_CLICK_ONGOING_CHALLENGE_TICKER,
            eventLabel = String.format(
                CreateReviewTrackingConstants.EVENT_LABEL_VIEW_ONGOING_CHALLENGE_TICKER,
                reputationId,
                orderId,
                productId
            )
        ).appendBusinessUnit(CreateReviewTrackingConstants.BUSINESS_UNIT)
            .appendCurrentSite(CreateReviewTrackingConstants.CURRENT_SITE)
            .appendUserId(userId)
            .sendGeneralEvent()
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
                event = ReviewTrackingConstant.VIEW_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                action = mapDialogTypeToViewDialogEventAction(dialogType),
                label = String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_VIEW_DIALOG,
                    title,
                    reputationId,
                    orderId,
                    productId
                ),
                productId = productId,
                userId = userId
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
                event = ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                action = mapDialogTypeToClickDialogEventAction(dialogType),
                label = String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_VIEW_DIALOG,
                    title,
                    reputationId,
                    orderId,
                    productId
                ),
                productId = productId,
                userId = userId
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
                event = ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                action = CreateReviewTrackingConstants.EVENT_ACTION_CLICK_SUBMIT,
                label = String.format(
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
                productId = productId,
                userId = userId
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
                event = ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                action = CreateReviewTrackingConstants.EVENT_ACTION_DISMISS_FORM,
                label = String.format(
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
                productId = productId,
                userId = userId
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
                event = ReviewTrackingConstant.VIEW_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                action = CreateReviewTrackingConstants.EVENT_ACTION_VIEW_THANK_YOU_BOTTOM_SHEET,
                label = String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_VIEW_THANK_YOU_BOTTOM_SHEET,
                    title,
                    reputationId,
                    orderId,
                    productId,
                    feedbackId
                ),
                productId = productId,
                userId = userId
            )
        )
    }

    fun eventViewPostSubmitReviewBottomSheetForOngoingChallenge(
        title: String,
        reputationId: String,
        orderId: String,
        feedbackId: String,
        productId: String,
        userId: String
    ) {
        Bundle().appendGeneralEventData(
            eventName = ReviewTrackingConstant.KEY_VIEW_ITEM,
            eventCategory = CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
            eventAction = CreateReviewTrackingConstants.EVENT_ACTION_VIEW_POST_SUBMIT_REVIEW_BOTTOM_SHEET,
            eventLabel = String.format(
                CreateReviewTrackingConstants.EVENT_LABEL_VIEW_POST_SUBMIT_REVIEW_BOTTOM_SHEET,
                title,
                reputationId,
                orderId,
                productId,
                feedbackId
            ),
        ).appendBusinessUnit(CreateReviewTrackingConstants.BUSINESS_UNIT)
            .appendCurrentSite(CreateReviewTrackingConstants.CURRENT_SITE)
            .appendPromotionsEE(title)
            .appendUserId(userId)
            .sendEnhancedEcommerce(ReviewTrackingConstant.KEY_VIEW_ITEM)
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
                event = ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                category = CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
                action = CreateReviewTrackingConstants.EVENT_ACTION_CLICK_BAD_RATING_REASON,
                label = String.format(
                    CreateReviewTrackingConstants.EVENT_LABEL_CLICK_BAD_RATING_REASON,
                    orderId,
                    productId,
                    badRatingReason,
                    isActive.toString()
                ),
                productId = productId,
                userId = userId
            )
        )
    }

    fun eventClickPostSubmitBottomSheetButton(
        bottomSheetTitle: String,
        ctaText: String,
        reputationId: String,
        orderId: String,
        productId: String,
        feedbackId: String,
        userId: String
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = ReviewTrackingConstant.EVENT_CLICK_REVIEW,
            eventCategory = CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
            eventAction = CreateReviewTrackingConstants.EVENT_ACTION_CLICK_THANK_YOU_BOTTOM_SHEET_BUTTON,
            eventLabel = String.format(
                CreateReviewTrackingConstants.EVENT_LABEL_CLICK_POST_SUBMIT_REVIEW_BOTTOM_SHEET_CTA,
                bottomSheetTitle,
                ctaText,
                reputationId,
                orderId,
                productId,
                feedbackId
            )
        ).appendBusinessUnit(CreateReviewTrackingConstants.BUSINESS_UNIT)
            .appendCurrentSite(CreateReviewTrackingConstants.CURRENT_SITE)
            .appendUserId(userId)
            .sendGeneralEvent()
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
        val eventMap = createEventMap(event, category, action, label, userId)
        eventMap[CreateReviewTrackingConstants.KEY_PRODUCT_ID] = productId
        return eventMap
    }

    private fun createEventMap(
        event: String,
        category: String,
        action: String,
        label: String,
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

    private fun Bundle.appendPromotionsEE(title: String): Bundle {
        val promotions = listOf(
            Bundle().apply {
                putString(AnalyticConstant.KEY_EE_CREATIVE_NAME, null)
                putString(AnalyticConstant.KEY_EE_CREATIVE_SLOT, null)
                putString(AnalyticConstant.KEY_EE_ITEM_ID, title)
                putString(AnalyticConstant.KEY_EE_ITEM_NAME, null)
            }
        )
        putParcelableArrayList(AnalyticConstant.KEY_EE_PROMOTIONS, ArrayList(promotions))
        return this
    }

    fun trackOpenUniversalMediaPicker(userId: String, shopId: String) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            CreateReviewTrackingConstants.EVENT_NAME_CLICK_COMMUNICATION,
            CreateReviewTrackingConstants.EVENT_CATEGORY_MEDIA_CAMERA,
            CreateReviewTrackingConstants.EVENT_ACTION_VISIT_CAMERA,
            String.format(
                CreateReviewTrackingConstants.EVENT_LABEL_OPEN_MEDIA_PICKER,
                PageSource.Review, userId, shopId
            )
        ).appendBusinessUnit(CreateReviewTrackingConstants.BUSINESS_UNIT_MEDIA)
            .appendCurrentSite(CreateReviewTrackingConstants.CURRENT_SITE)
            .appendUserId(userId)
            .sendGeneralEvent()
    }

    fun trackErrorSubmitReview(
        userId: String,
        errorMessage: String,
        orderId: String,
        productId: String,
        rating: Int,
        hasReviewText: Boolean,
        reviewTextLength: Int,
        mediaCount: Int,
        anonymous: Boolean,
        hasIncentive: Boolean,
        hasTemplate: Boolean,
        templateUsedCount: Int
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            CreateReviewTrackingConstants.EVENT_NAME_CLICK_PG,
            CreateReviewTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
            CreateReviewTrackingConstants.EVENT_ACTION_CLICK_SUBMIT_ERROR,
            String.format(
                CreateReviewTrackingConstants.EVENT_LABEL_CLICK_SUBMIT_ERROR,
                errorMessage,
                orderId,
                productId,
                rating,
                if (hasReviewText) "filled" else "blank",
                reviewTextLength,
                mediaCount,
                anonymous,
                hasIncentive,
                hasTemplate,
                templateUsedCount
            )
        ).appendBusinessUnit(CreateReviewTrackingConstants.BUSINESS_UNIT)
            .appendCurrentSite(CreateReviewTrackingConstants.CURRENT_SITE)
            .appendUserId(userId)
            .appendProductId(productId)
            .sendGeneralEvent()
    }
}