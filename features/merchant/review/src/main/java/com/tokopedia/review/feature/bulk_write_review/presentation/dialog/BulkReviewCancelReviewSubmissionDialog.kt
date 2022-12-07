package com.tokopedia.review.feature.bulk_write_review.presentation.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.review.R
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewCancelReviewSubmissionDialogUiState

class BulkReviewCancelReviewSubmissionDialog(
    context: Context,
    private val listener: Listener
) {
    private val dialog by lazy(LazyThreadSafetyMode.NONE) {
        DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(context.getString(R.string.bulk_review_cancel_review_submission_dialog_title))
            setDescription(context.getString(R.string.bulk_review_cancel_review_submission_dialog_description))
            setPrimaryCTAText(context.getString(R.string.bulk_review_cancel_review_submission_dialog_primary_cta_text))
            setSecondaryCTAText(context.getString(R.string.bulk_review_cancel_review_submission_dialog_secondary_cta_text))
            setSecondaryCTAClickListener { listener.onCancelReviewSubmissionCancellation() }
            setOverlayClose(false)
            setCancelable(false)
        }
    }

    fun updateUiState(uiState: BulkReviewCancelReviewSubmissionDialogUiState) {
        when (uiState) {
            is BulkReviewCancelReviewSubmissionDialogUiState.Dismissed -> dismiss()
            is BulkReviewCancelReviewSubmissionDialogUiState.Showing -> show()
        }
    }

    private fun show() {
        dialog.setPrimaryCTAClickListener { listener.onConfirmCancelReviewSubmission() }
        dialog.show()
    }

    private fun dismiss() {
        dialog.dismiss()
    }

    interface Listener {
        fun onConfirmCancelReviewSubmission()
        fun onCancelReviewSubmissionCancellation()
    }
}
