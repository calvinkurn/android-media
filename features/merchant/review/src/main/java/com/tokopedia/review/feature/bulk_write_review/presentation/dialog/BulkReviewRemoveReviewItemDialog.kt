package com.tokopedia.review.feature.bulk_write_review.presentation.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.review.R
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRemoveReviewItemDialogUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.util.ResourceProvider

class BulkReviewRemoveReviewItemDialog(
    private val context: Context,
    private val listener: Listener
) {
    private val dialog by lazy(LazyThreadSafetyMode.NONE) {
        DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(ResourceProvider.getRemoveReviewItemDialogTitle().getStringValue(context))
            setDescription(ResourceProvider.getRemoveReviewItemDialogSubtitle().getStringValue(context))
            setPrimaryCTAText(context.getString(R.string.bulk_review_remove_review_item_dialog_primary_cta_text))
            setSecondaryCTAText(context.getString(R.string.bulk_review_remove_review_item_dialog_secondary_cta_text))
            setOverlayClose(false)
            setCancelable(false)
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
            listener.onConfirmRemoveReviewItem(
                inboxID = inboxID,
                title = ResourceProvider.getRemoveReviewItemDialogTitle().getStringValue(context),
                subtitle = ResourceProvider.getRemoveReviewItemDialogSubtitle().getStringValue(context)
            )
        }
        dialog.setSecondaryCTAClickListener {
            listener.onCancelRemoveReviewItem(
                title = ResourceProvider.getRemoveReviewItemDialogTitle().getStringValue(context),
                subtitle = ResourceProvider.getRemoveReviewItemDialogSubtitle().getStringValue(context)
            )
        }
        dialog.setOnShowListener {
            listener.onRemoveReviewItemDialogImpressed(
                inboxID = inboxID,
                title = ResourceProvider.getRemoveReviewItemDialogTitle().getStringValue(context),
                subtitle = ResourceProvider.getRemoveReviewItemDialogSubtitle().getStringValue(context)
            )
        }
        dialog.show()
    }

    private fun dismiss() {
        dialog.dismiss()
    }

    interface Listener {
        fun onConfirmRemoveReviewItem(inboxID: String, title: String, subtitle: String)
        fun onCancelRemoveReviewItem(title: String, subtitle: String)
        fun onRemoveReviewItemDialogImpressed(inboxID: String, title: String, subtitle: String)
    }
}
