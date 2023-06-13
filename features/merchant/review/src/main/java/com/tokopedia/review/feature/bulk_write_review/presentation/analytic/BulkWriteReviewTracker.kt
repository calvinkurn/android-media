package com.tokopedia.review.feature.bulk_write_review.presentation.analytic

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.reviewcommon.uimodel.StringRes
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

/**
 * Tracking request link: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3658
 */
class BulkWriteReviewTracker @Inject constructor(
    private val trackingQueue: TrackingQueue,
    @ApplicationContext private val context: Context
) {
    fun trackReviewItemImpression(
        position: Int,
        orderId: String,
        reputationId: String,
        productId: String,
        userId: String
    ) {
        trackingQueue.putEETracking(
            hashMapOf(
                ReviewTrackingConstant.EVENT to BulkWriteReviewTrackingConstant.EVENT_NAME_PROMO_VIEW,
                ReviewTrackingConstant.EVENT_ACTION to BulkWriteReviewTrackingConstant.EVENT_ACTION_BULK_REVIEW_FORM_IMPRESSION,
                ReviewTrackingConstant.EVENT_LABEL to String.EMPTY,
                ReviewTrackingConstant.EVENT_CATEGORY to BulkWriteReviewTrackingConstant.EVENT_CATEGORY_BULK_REVIEW_FORM,
                ReviewTrackingConstant.KEY_TRACKER_ID to BulkWriteReviewTrackingConstant.TRACKER_ID_BULK_REVIEW_FORM_IMPRESSION,
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to userId,
                ReviewTrackingConstant.KEY_ECOMMERCE to hashMapOf(
                    ReviewTrackingConstant.EVENT_PROMO_VIEW to hashMapOf(
                        ReviewTrackingConstant.KEY_PROMOTIONS to arrayListOf(
                            hashMapOf(
                                ReviewTrackingConstant.KEY_CREATIVE_NAME to null.toString(),
                                ReviewTrackingConstant.KEY_CREATIVE_SLOT to position,
                                ReviewTrackingConstant.KEY_ITEM_ID to listOf(
                                    orderId,
                                    reputationId,
                                    productId
                                ).joinToString(BulkWriteReviewTrackingConstant.SEPARATOR_SPACED_DASH),
                                ReviewTrackingConstant.KEY_ITEM_NAME to null.toString()
                            )
                        )
                    )
                )
            )
        )
    }

    fun trackReviewItemSubmission(
        position: Int,
        orderId: String,
        reputationId: String,
        productId: String,
        userId: String,
        rating: Int,
        isReviewEmpty: Boolean,
        reviewLength: Int,
        imageAttachmentCount: Int,
        videoAttachmentCount: Int,
        isAnonymous: Boolean
    ) {
        trackingQueue.putEETracking(
            hashMapOf(
                ReviewTrackingConstant.EVENT to BulkWriteReviewTrackingConstant.EVENT_NAME_PROMO_VIEW,
                ReviewTrackingConstant.EVENT_ACTION to BulkWriteReviewTrackingConstant.EVENT_ACTION_BULK_REVIEW_FORM_SUBMITTED,
                ReviewTrackingConstant.EVENT_LABEL to String.EMPTY,
                ReviewTrackingConstant.EVENT_CATEGORY to BulkWriteReviewTrackingConstant.EVENT_CATEGORY_BULK_REVIEW_FORM,
                ReviewTrackingConstant.KEY_TRACKER_ID to BulkWriteReviewTrackingConstant.TRACKER_ID_BULK_REVIEW_FORM_SUBMITTED,
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to userId,
                ReviewTrackingConstant.KEY_ECOMMERCE to hashMapOf(
                    ReviewTrackingConstant.EVENT_PROMO_VIEW to hashMapOf(
                        ReviewTrackingConstant.KEY_PROMOTIONS to arrayListOf(
                            hashMapOf(
                                ReviewTrackingConstant.KEY_CREATIVE_NAME to String.format(
                                    BulkWriteReviewTrackingConstant.CREATIVE_NAME_FORMAT_REVIEW_FORM_SUBMITTED,
                                    rating,
                                    if (isReviewEmpty) BulkWriteReviewTrackingConstant.BLANK else BulkWriteReviewTrackingConstant.FILLED,
                                    reviewLength,
                                    imageAttachmentCount,
                                    videoAttachmentCount,
                                    isAnonymous,
                                    Int.ZERO
                                ),
                                ReviewTrackingConstant.KEY_CREATIVE_SLOT to position,
                                ReviewTrackingConstant.KEY_ITEM_ID to listOf(
                                    orderId,
                                    reputationId,
                                    productId
                                ).joinToString(BulkWriteReviewTrackingConstant.SEPARATOR_SPACED_DASH),
                                ReviewTrackingConstant.KEY_ITEM_NAME to null.toString()
                            )
                        )
                    )
                )
            )
        )
    }

    fun trackPageDismissal(
        position: Int,
        orderId: String,
        reputationId: String,
        productId: String,
        userId: String,
        rating: Int,
        isReviewEmpty: Boolean,
        reviewLength: Int,
        imageAttachmentCount: Int,
        videoAttachmentCount: Int,
        isAnonymous: Boolean
    ) {
        trackingQueue.putEETracking(
            hashMapOf(
                ReviewTrackingConstant.EVENT to BulkWriteReviewTrackingConstant.EVENT_NAME_PROMO_VIEW,
                ReviewTrackingConstant.EVENT_ACTION to BulkWriteReviewTrackingConstant.EVENT_ACTION_BULK_REVIEW_FORM_DISMISS,
                ReviewTrackingConstant.EVENT_LABEL to String.EMPTY,
                ReviewTrackingConstant.EVENT_CATEGORY to BulkWriteReviewTrackingConstant.EVENT_CATEGORY_BULK_REVIEW_FORM,
                ReviewTrackingConstant.KEY_TRACKER_ID to BulkWriteReviewTrackingConstant.TRACKER_ID_BULK_REVIEW_FORM_DISMISS,
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to userId,
                ReviewTrackingConstant.KEY_ECOMMERCE to hashMapOf(
                    ReviewTrackingConstant.EVENT_PROMO_VIEW to hashMapOf(
                        ReviewTrackingConstant.KEY_PROMOTIONS to arrayListOf(
                            hashMapOf(
                                ReviewTrackingConstant.KEY_CREATIVE_NAME to String.format(
                                    BulkWriteReviewTrackingConstant.CREATIVE_NAME_FORMAT_REVIEW_FORM_DISMISS,
                                    rating,
                                    if (isReviewEmpty) BulkWriteReviewTrackingConstant.BLANK else BulkWriteReviewTrackingConstant.FILLED,
                                    reviewLength,
                                    imageAttachmentCount,
                                    videoAttachmentCount,
                                    isAnonymous,
                                    Int.ZERO
                                ),
                                ReviewTrackingConstant.KEY_CREATIVE_SLOT to position,
                                ReviewTrackingConstant.KEY_ITEM_ID to listOf(
                                    orderId,
                                    reputationId,
                                    productId
                                ).joinToString(BulkWriteReviewTrackingConstant.SEPARATOR_SPACED_DASH),
                                ReviewTrackingConstant.KEY_ITEM_NAME to null.toString()
                            )
                        )
                    )
                )
            )
        )
    }

    fun trackReviewItemBadRatingCategoryImpression(
        position: Int,
        orderId: String,
        reputationId: String,
        productId: String,
        userId: String,
        rating: Int,
        reason: String
    ) {
        trackingQueue.putEETracking(
            hashMapOf(
                ReviewTrackingConstant.EVENT to BulkWriteReviewTrackingConstant.EVENT_NAME_PROMO_VIEW,
                ReviewTrackingConstant.EVENT_ACTION to BulkWriteReviewTrackingConstant.EVENT_ACTION_BULK_REVIEW_FORM_BAD_RATING_CATEGORY_IMPRESSION,
                ReviewTrackingConstant.EVENT_LABEL to String.format(
                    BulkWriteReviewTrackingConstant.EVENT_LABEL_FORMAT_REVIEW_FORM_BAD_RATING_CATEGORY_IMPRESSION,
                    rating
                ),
                ReviewTrackingConstant.EVENT_CATEGORY to BulkWriteReviewTrackingConstant.EVENT_CATEGORY_BULK_REVIEW_FORM,
                ReviewTrackingConstant.KEY_TRACKER_ID to BulkWriteReviewTrackingConstant.TRACKER_ID_BULK_REVIEW_FORM_BAD_RATING_CATEGORY_IMPRESSION,
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to userId,
                ReviewTrackingConstant.KEY_ECOMMERCE to hashMapOf(
                    ReviewTrackingConstant.EVENT_PROMO_VIEW to hashMapOf(
                        ReviewTrackingConstant.KEY_PROMOTIONS to arrayListOf(
                            hashMapOf(
                                ReviewTrackingConstant.KEY_CREATIVE_NAME to null.toString(),
                                ReviewTrackingConstant.KEY_CREATIVE_SLOT to position,
                                ReviewTrackingConstant.KEY_ITEM_ID to listOf(
                                    orderId,
                                    reputationId,
                                    productId
                                ).joinToString(BulkWriteReviewTrackingConstant.SEPARATOR_SPACED_DASH),
                                ReviewTrackingConstant.KEY_ITEM_NAME to reason
                            )
                        )
                    )
                )
            )
        )
    }

    fun trackReviewItemBadRatingCategorySelected(
        position: Int,
        orderId: String,
        reputationId: String,
        productId: String,
        userId: String,
        rating: Int,
        reason: String
    ) {
        trackingQueue.putEETracking(
            hashMapOf(
                ReviewTrackingConstant.EVENT to BulkWriteReviewTrackingConstant.EVENT_NAME_PROMO_VIEW,
                ReviewTrackingConstant.EVENT_ACTION to BulkWriteReviewTrackingConstant.EVENT_ACTION_BULK_REVIEW_FORM_BAD_RATING_CATEGORY_SELECTED,
                ReviewTrackingConstant.EVENT_LABEL to String.format(
                    BulkWriteReviewTrackingConstant.EVENT_LABEL_FORMAT_REVIEW_FORM_BAD_RATING_CATEGORY_SELECTED,
                    rating
                ),
                ReviewTrackingConstant.EVENT_CATEGORY to BulkWriteReviewTrackingConstant.EVENT_CATEGORY_BULK_REVIEW_FORM,
                ReviewTrackingConstant.KEY_TRACKER_ID to BulkWriteReviewTrackingConstant.TRACKER_ID_BULK_REVIEW_FORM_BAD_RATING_CATEGORY_SELECTED,
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to userId,
                ReviewTrackingConstant.KEY_ECOMMERCE to hashMapOf(
                    ReviewTrackingConstant.EVENT_PROMO_VIEW to hashMapOf(
                        ReviewTrackingConstant.KEY_PROMOTIONS to arrayListOf(
                            hashMapOf(
                                ReviewTrackingConstant.KEY_CREATIVE_NAME to null.toString(),
                                ReviewTrackingConstant.KEY_CREATIVE_SLOT to position,
                                ReviewTrackingConstant.KEY_ITEM_ID to listOf(
                                    orderId,
                                    reputationId,
                                    productId
                                ).joinToString(BulkWriteReviewTrackingConstant.SEPARATOR_SPACED_DASH),
                                ReviewTrackingConstant.KEY_ITEM_NAME to reason
                            )
                        )
                    )
                )
            )
        )
    }

    fun trackReviewItemSubmissionError(
        position: Int,
        orderId: String,
        reputationId: String,
        productId: String,
        userId: String,
        errorMessage: StringRes,
        rating: Int,
        isReviewEmpty: Boolean,
        reviewLength: Int,
        imageAttachmentCount: Int,
        videoAttachmentCount: Int,
        isAnonymous: Boolean
    ) {
        trackingQueue.putEETracking(
            hashMapOf(
                ReviewTrackingConstant.EVENT to BulkWriteReviewTrackingConstant.EVENT_NAME_PROMO_VIEW,
                ReviewTrackingConstant.EVENT_ACTION to BulkWriteReviewTrackingConstant.EVENT_ACTION_BULK_REVIEW_FORM_SUBMIT_ERROR,
                ReviewTrackingConstant.EVENT_LABEL to String.format(
                    BulkWriteReviewTrackingConstant.EVENT_LABEL_FORMAT_REVIEW_FORM_SUBMIT_ERROR,
                    errorMessage.getStringValueWithDefaultParam(context)
                ),
                ReviewTrackingConstant.EVENT_CATEGORY to BulkWriteReviewTrackingConstant.EVENT_CATEGORY_BULK_REVIEW_FORM,
                ReviewTrackingConstant.KEY_TRACKER_ID to BulkWriteReviewTrackingConstant.TRACKER_ID_BULK_REVIEW_FORM_SUBMIT_ERROR,
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to userId,
                ReviewTrackingConstant.KEY_ECOMMERCE to hashMapOf(
                    ReviewTrackingConstant.EVENT_PROMO_VIEW to hashMapOf(
                        ReviewTrackingConstant.KEY_PROMOTIONS to arrayListOf(
                            hashMapOf(
                                ReviewTrackingConstant.KEY_CREATIVE_NAME to String.format(
                                    BulkWriteReviewTrackingConstant.CREATIVE_NAME_FORMAT_REVIEW_FORM_SUBMIT_ERROR,
                                    rating,
                                    if (isReviewEmpty) BulkWriteReviewTrackingConstant.BLANK else BulkWriteReviewTrackingConstant.FILLED,
                                    reviewLength,
                                    imageAttachmentCount,
                                    videoAttachmentCount,
                                    isAnonymous,
                                    Int.ZERO
                                ),
                                ReviewTrackingConstant.KEY_CREATIVE_SLOT to position,
                                ReviewTrackingConstant.KEY_ITEM_ID to listOf(
                                    orderId,
                                    reputationId,
                                    productId
                                ).joinToString(BulkWriteReviewTrackingConstant.SEPARATOR_SPACED_DASH),
                                ReviewTrackingConstant.KEY_ITEM_NAME to null.toString()
                            )
                        )
                    )
                )
            )
        )
    }

    fun trackReviewItemRemoveButtonClick(
        position: Int,
        orderId: String,
        reputationId: String,
        productId: String,
        userId: String
    ) {
        trackingQueue.putEETracking(
            hashMapOf(
                ReviewTrackingConstant.EVENT to BulkWriteReviewTrackingConstant.EVENT_NAME_PROMO_VIEW,
                ReviewTrackingConstant.EVENT_ACTION to BulkWriteReviewTrackingConstant.EVENT_ACTION_BULK_REVIEW_FORM_REMOVE_BUTTON_CLICK,
                ReviewTrackingConstant.EVENT_LABEL to String.EMPTY,
                ReviewTrackingConstant.EVENT_CATEGORY to BulkWriteReviewTrackingConstant.EVENT_CATEGORY_BULK_REVIEW_FORM,
                ReviewTrackingConstant.KEY_TRACKER_ID to BulkWriteReviewTrackingConstant.TRACKER_ID_BULK_REVIEW_FORM_REMOVE_BUTTON_CLICK,
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to userId,
                ReviewTrackingConstant.KEY_ECOMMERCE to hashMapOf(
                    ReviewTrackingConstant.EVENT_PROMO_VIEW to hashMapOf(
                        ReviewTrackingConstant.KEY_PROMOTIONS to arrayListOf(
                            hashMapOf(
                                ReviewTrackingConstant.KEY_CREATIVE_NAME to null.toString(),
                                ReviewTrackingConstant.KEY_CREATIVE_SLOT to position,
                                ReviewTrackingConstant.KEY_ITEM_ID to listOf(
                                    orderId,
                                    reputationId,
                                    productId
                                ).joinToString(BulkWriteReviewTrackingConstant.SEPARATOR_SPACED_DASH),
                                ReviewTrackingConstant.KEY_ITEM_NAME to null.toString()
                            )
                        )
                    )
                )
            )
        )
    }

    fun trackRemoveReviewItemDialogImpression(
        position: Int,
        orderId: String,
        reputationId: String,
        productId: String,
        userId: String,
        title: String,
        subtitle: String
    ) {
        trackingQueue.putEETracking(
            hashMapOf(
                ReviewTrackingConstant.EVENT to BulkWriteReviewTrackingConstant.EVENT_NAME_PROMO_VIEW,
                ReviewTrackingConstant.EVENT_ACTION to BulkWriteReviewTrackingConstant.EVENT_ACTION_BULK_REVIEW_FORM_REMOVE_DIALOG_IMPRESSION,
                ReviewTrackingConstant.EVENT_LABEL to String.format(
                    BulkWriteReviewTrackingConstant.EVENT_LABEL_FORMAT_REVIEW_FORM_REMOVE_DIALOG_IMPRESSION,
                    title,
                    subtitle
                ),
                ReviewTrackingConstant.EVENT_CATEGORY to BulkWriteReviewTrackingConstant.EVENT_CATEGORY_BULK_REVIEW_FORM,
                ReviewTrackingConstant.KEY_TRACKER_ID to BulkWriteReviewTrackingConstant.TRACKER_ID_BULK_REVIEW_FORM_REMOVE_DIALOG_IMPRESSION,
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to userId,
                ReviewTrackingConstant.KEY_ECOMMERCE to hashMapOf(
                    ReviewTrackingConstant.EVENT_PROMO_VIEW to hashMapOf(
                        ReviewTrackingConstant.KEY_PROMOTIONS to arrayListOf(
                            hashMapOf(
                                ReviewTrackingConstant.KEY_CREATIVE_NAME to null.toString(),
                                ReviewTrackingConstant.KEY_CREATIVE_SLOT to position,
                                ReviewTrackingConstant.KEY_ITEM_ID to listOf(
                                    orderId,
                                    reputationId,
                                    productId
                                ).joinToString(BulkWriteReviewTrackingConstant.SEPARATOR_SPACED_DASH),
                                ReviewTrackingConstant.KEY_ITEM_NAME to null.toString()
                            )
                        )
                    )
                )
            )
        )
    }

    fun trackRemoveReviewItemDialogCancelButtonClick(
        userId: String,
        title: String,
        subtitle: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                ReviewTrackingConstant.EVENT to BulkWriteReviewTrackingConstant.EVENT_NAME_CLICK_PG,
                ReviewTrackingConstant.EVENT_ACTION to BulkWriteReviewTrackingConstant.EVENT_ACTION_BULK_REVIEW_FORM_REMOVE_DIALOG_CANCEL_BUTTON_CLICK,
                ReviewTrackingConstant.EVENT_LABEL to String.format(
                    BulkWriteReviewTrackingConstant.EVENT_LABEL_FORMAT_REVIEW_FORM_REMOVE_DIALOG_BUTTON_CLICK,
                    title,
                    subtitle
                ),
                ReviewTrackingConstant.EVENT_CATEGORY to BulkWriteReviewTrackingConstant.EVENT_CATEGORY_BULK_REVIEW_FORM,
                ReviewTrackingConstant.KEY_TRACKER_ID to BulkWriteReviewTrackingConstant.TRACKER_ID_BULK_REVIEW_FORM_REMOVE_DIALOG_CANCEL_BUTTON_CLICK,
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to userId
            )
        )
    }

    fun trackRemoveReviewItemDialogRemoveButtonClick(
        userId: String,
        title: String,
        subtitle: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                ReviewTrackingConstant.EVENT to BulkWriteReviewTrackingConstant.EVENT_NAME_CLICK_PG,
                ReviewTrackingConstant.EVENT_ACTION to BulkWriteReviewTrackingConstant.EVENT_ACTION_BULK_REVIEW_FORM_REMOVE_DIALOG_REMOVE_BUTTON_CLICK,
                ReviewTrackingConstant.EVENT_LABEL to String.format(
                    BulkWriteReviewTrackingConstant.EVENT_LABEL_FORMAT_REVIEW_FORM_REMOVE_DIALOG_BUTTON_CLICK,
                    title,
                    subtitle
                ),
                ReviewTrackingConstant.EVENT_CATEGORY to BulkWriteReviewTrackingConstant.EVENT_CATEGORY_BULK_REVIEW_FORM,
                ReviewTrackingConstant.KEY_TRACKER_ID to BulkWriteReviewTrackingConstant.TRACKER_ID_BULK_REVIEW_FORM_REMOVE_DIALOG_REMOVE_BUTTON_CLICK,
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to userId
            )
        )
    }

    fun trackReviewItemAddTestimonyMiniActionClick(
        position: Int,
        orderId: String,
        reputationId: String,
        productId: String,
        userId: String
    ) {
        trackingQueue.putEETracking(
            hashMapOf(
                ReviewTrackingConstant.EVENT to BulkWriteReviewTrackingConstant.EVENT_NAME_PROMO_VIEW,
                ReviewTrackingConstant.EVENT_ACTION to BulkWriteReviewTrackingConstant.EVENT_ACTION_BULK_REVIEW_FORM_ADD_TESTIMONY_CLICK,
                ReviewTrackingConstant.EVENT_LABEL to String.EMPTY,
                ReviewTrackingConstant.EVENT_CATEGORY to BulkWriteReviewTrackingConstant.EVENT_CATEGORY_BULK_REVIEW_FORM,
                ReviewTrackingConstant.KEY_TRACKER_ID to BulkWriteReviewTrackingConstant.TRACKER_ID_BULK_REVIEW_FORM_ADD_TESTIMONY_CLICK,
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to userId,
                ReviewTrackingConstant.KEY_ECOMMERCE to hashMapOf(
                    ReviewTrackingConstant.EVENT_PROMO_VIEW to hashMapOf(
                        ReviewTrackingConstant.KEY_PROMOTIONS to arrayListOf(
                            hashMapOf(
                                ReviewTrackingConstant.KEY_CREATIVE_NAME to null.toString(),
                                ReviewTrackingConstant.KEY_CREATIVE_SLOT to position,
                                ReviewTrackingConstant.KEY_ITEM_ID to listOf(
                                    orderId,
                                    reputationId,
                                    productId
                                ).joinToString(BulkWriteReviewTrackingConstant.SEPARATOR_SPACED_DASH),
                                ReviewTrackingConstant.KEY_ITEM_NAME to null.toString()
                            )
                        )
                    )
                )
            )
        )
    }

    fun trackReviewItemAddAttachmentMiniActionClick(
        position: Int,
        orderId: String,
        reputationId: String,
        productId: String,
        userId: String
    ) {
        trackingQueue.putEETracking(
            hashMapOf(
                ReviewTrackingConstant.EVENT to BulkWriteReviewTrackingConstant.EVENT_NAME_PROMO_VIEW,
                ReviewTrackingConstant.EVENT_ACTION to BulkWriteReviewTrackingConstant.EVENT_ACTION_BULK_REVIEW_FORM_ADD_ATTACHMENT_CLICK,
                ReviewTrackingConstant.EVENT_LABEL to String.EMPTY,
                ReviewTrackingConstant.EVENT_CATEGORY to BulkWriteReviewTrackingConstant.EVENT_CATEGORY_BULK_REVIEW_FORM,
                ReviewTrackingConstant.KEY_TRACKER_ID to BulkWriteReviewTrackingConstant.TRACKER_ID_BULK_REVIEW_FORM_ADD_ATTACHMENT_CLICK,
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to userId,
                ReviewTrackingConstant.KEY_ECOMMERCE to hashMapOf(
                    ReviewTrackingConstant.EVENT_PROMO_VIEW to hashMapOf(
                        ReviewTrackingConstant.KEY_PROMOTIONS to arrayListOf(
                            hashMapOf(
                                ReviewTrackingConstant.KEY_CREATIVE_NAME to null.toString(),
                                ReviewTrackingConstant.KEY_CREATIVE_SLOT to position,
                                ReviewTrackingConstant.KEY_ITEM_ID to listOf(
                                    orderId,
                                    reputationId,
                                    productId
                                ).joinToString(BulkWriteReviewTrackingConstant.SEPARATOR_SPACED_DASH),
                                ReviewTrackingConstant.KEY_ITEM_NAME to null.toString()
                            )
                        )
                    )
                )
            )
        )
    }

    fun trackReviewItemRatingChanged(
        position: Int,
        orderId: String,
        reputationId: String,
        productId: String,
        userId: String,
        rating: Int,
        timestamp: Long
    ) {
        trackingQueue.putEETracking(
            hashMapOf(
                ReviewTrackingConstant.EVENT to BulkWriteReviewTrackingConstant.EVENT_NAME_PROMO_VIEW,
                ReviewTrackingConstant.EVENT_ACTION to BulkWriteReviewTrackingConstant.EVENT_ACTION_BULK_REVIEW_FORM_CHANGE_RATING,
                ReviewTrackingConstant.EVENT_LABEL to String.EMPTY,
                ReviewTrackingConstant.EVENT_CATEGORY to BulkWriteReviewTrackingConstant.EVENT_CATEGORY_BULK_REVIEW_FORM,
                ReviewTrackingConstant.KEY_TRACKER_ID to BulkWriteReviewTrackingConstant.TRACKER_ID_BULK_REVIEW_FORM_CHANGE_RATING,
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to userId,
                ReviewTrackingConstant.KEY_ECOMMERCE to hashMapOf(
                    ReviewTrackingConstant.EVENT_PROMO_VIEW to hashMapOf(
                        ReviewTrackingConstant.KEY_PROMOTIONS to arrayListOf(
                            hashMapOf(
                                ReviewTrackingConstant.KEY_CREATIVE_NAME to String.format(
                                    BulkWriteReviewTrackingConstant.CREATIVE_NAME_FORMAT_REVIEW_FORM_CHANGE_RATING,
                                    rating,
                                    timestamp
                                ),
                                ReviewTrackingConstant.KEY_CREATIVE_SLOT to position,
                                ReviewTrackingConstant.KEY_ITEM_ID to listOf(
                                    orderId,
                                    reputationId,
                                    productId
                                ).joinToString(BulkWriteReviewTrackingConstant.SEPARATOR_SPACED_DASH),
                                ReviewTrackingConstant.KEY_ITEM_NAME to null.toString()
                            )
                        )
                    )
                )
            )
        )
    }

    fun sendAllTrackers() {
        trackingQueue.sendAll()
    }
}
