package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel

object AdapterDiffCallback {

    val ImageReviewDiffCallback = object : DiffUtil.ItemCallback<FeedbackUiModel.Attachment>() {
        override fun areItemsTheSame(oldItemReviewFeedback: FeedbackUiModel.Attachment, newItemReviewFeedback: FeedbackUiModel.Attachment): Boolean {
            return oldItemReviewFeedback == newItemReviewFeedback
        }

        override fun areContentsTheSame(oldItemReviewFeedback: FeedbackUiModel.Attachment, newItemReviewFeedback: FeedbackUiModel.Attachment): Boolean {
            return oldItemReviewFeedback == newItemReviewFeedback
        }
    }
}