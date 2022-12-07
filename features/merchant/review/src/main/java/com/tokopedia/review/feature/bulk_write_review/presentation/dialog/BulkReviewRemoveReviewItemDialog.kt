package com.tokopedia.review.feature.bulk_write_review.presentation.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.review.R
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRemoveReviewItemDialogUiState

class BulkReviewRemoveReviewItemDialog(
    context: Context,
    private val listener: Listener
) {
    private val dialog by lazy(LazyThreadSafetyMode.NONE) {
        DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(context.getString(R.string.bulk_review_remove_review_item_dialog_title))
            setDescription(context.getString(R.string.bulk_review_remove_review_item_dialog_description))
            setPrimaryCTAText(context.getString(R.string.bulk_review_remove_review_item_dialog_primary_cta_text))
            setSecondaryCTAText(context.getString(R.string.bulk_review_remove_review_item_dialog_secondary_cta_text))
            setSecondaryCTAClickListener { listener.onCancelRemoveReviewItem() }
            setOverlayClose(false)
        }
    }

    fun updateUiState(uiState: BulkReviewRemoveReviewItemDialogUiState) {
        when (uiState) {
            is BulkReviewRemoveReviewItemDialogUiState.Dismissed -> dismiss()
            is BulkReviewRemoveReviewItemDialogUiState.Showing -> show(uiState.inboxID)
        }
    }

    private fun show(inboxID: String) {
        dialog.setPrimaryCTAClickListener {
            listener.onConfirmRemoveReviewItem(inboxID)
        }
        dialog.show()
    }

    private fun dismiss() {
        dialog.dismiss()
    }

    interface Listener {
        fun onConfirmRemoveReviewItem(inboxID: String)
        fun onCancelRemoveReviewItem()
    }
}
