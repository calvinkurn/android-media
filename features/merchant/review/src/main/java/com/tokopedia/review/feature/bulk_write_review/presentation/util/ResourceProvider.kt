package com.tokopedia.review.feature.bulk_write_review.presentation.util

import com.tokopedia.review.R
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.reviewcommon.uimodel.StringRes

object ResourceProvider {
    fun getErrorMessageCannotAddMediaWhileUploading(): StringRes {
        return StringRes(R.string.review_form_cannot_add_more_media_while_uploading)
    }

    fun getMessageReviewItemPartiallySubmitted(): StringRes {
        return StringRes(R.string.toaster_bulk_review_success_submit_reviews_partially)
    }

    fun getMessageReviewItemSubmissionFullyError(): StringRes {
        return StringRes(R.string.toaster_bulk_review_failed_submit_reviews)
    }

    fun getCtaOke(): StringRes {
        return StringRes(R.string.review_oke)
    }

    fun getCtaCancel(): StringRes {
        return StringRes(R.string.review_cancel)
    }

    fun getBadRatingCategoryExpandedTextAreaHint(): StringRes {
        return StringRes(R.string.hint_bulk_review_bad_rating_category_expanded_text_area)
    }

    fun getExpandedTextAreaTitle(rating: Int?): StringRes {
        return when (rating) {
            ReviewConstants.RATING_1 -> StringRes(R.string.review_create_worst_title)
            ReviewConstants.RATING_2 -> StringRes(R.string.review_form_bad_title)
            ReviewConstants.RATING_3 -> StringRes(R.string.review_form_neutral_title)
            else -> StringRes(R.string.review_create_best_title)
        }
    }

    fun getMessageFailedUploadMedia(errorCode: String): StringRes {
        return StringRes(R.string.review_form_media_picker_toaster_failed_upload_message, listOf(errorCode))
    }

    fun getMessageWaitForUploadMedia(): StringRes {
        return StringRes(R.string.review_form_media_picker_toaster_wait_for_upload_message)
    }

    fun getMessageBadRatingReasonCannotEmpty(): StringRes {
        return StringRes(R.string.toaster_bulk_review_bad_rating_category_reason_cannot_be_empty)
    }

    fun getMessageCannotRemoveMoreReviewItem(minReviewItem: Int): StringRes {
        return StringRes(R.string.toaster_bulk_review_cannot_remove_review_item, listOf(minReviewItem))
    }

    fun getMessageReviewItemRemoved(): StringRes {
        return StringRes(R.string.toaster_bulk_review_success_remove_review_item)
    }

    fun getRemoveReviewItemDialogTitle(): StringRes {
        return StringRes(R.string.bulk_review_remove_review_item_dialog_title)
    }

    fun getRemoveReviewItemDialogSubtitle(): StringRes {
        return StringRes(R.string.bulk_review_remove_review_item_dialog_description)
    }
}
