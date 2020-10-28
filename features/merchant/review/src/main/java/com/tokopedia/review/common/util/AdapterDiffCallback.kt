package com.tokopedia.review.common.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.review.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.review.feature.reviewreply.view.model.ReplyTemplateUiModel

object AdapterDiffCallback {

    val ImageReviewDiffCallback = object : DiffUtil.ItemCallback<FeedbackUiModel.Attachment>() {
        override fun areItemsTheSame(oldItemReviewFeedback: FeedbackUiModel.Attachment, newItemReviewFeedback: FeedbackUiModel.Attachment): Boolean {
            return oldItemReviewFeedback == newItemReviewFeedback
        }

        override fun areContentsTheSame(oldItemReviewFeedback: FeedbackUiModel.Attachment, newItemReviewFeedback: FeedbackUiModel.Attachment): Boolean {
            return oldItemReviewFeedback == newItemReviewFeedback
        }
    }

    val ImageInboxReviewDiffCallback = object : DiffUtil.ItemCallback<FeedbackInboxUiModel.Attachment>() {
        override fun areItemsTheSame(oldItemReviewFeedback: FeedbackInboxUiModel.Attachment, newItemReviewFeedback: FeedbackInboxUiModel.Attachment): Boolean {
            return oldItemReviewFeedback == newItemReviewFeedback
        }

        override fun areContentsTheSame(oldItemReviewFeedback: FeedbackInboxUiModel.Attachment, newItemReviewFeedback: FeedbackInboxUiModel.Attachment): Boolean {
            return oldItemReviewFeedback == newItemReviewFeedback
        }
    }

    val replyTemplateDiffCallback = object: DiffUtil.ItemCallback<ReplyTemplateUiModel>() {
        override fun areItemsTheSame(oldItemReplyTemplate: ReplyTemplateUiModel, newItemReplyTemplate: ReplyTemplateUiModel): Boolean {
            return oldItemReplyTemplate == newItemReplyTemplate
        }

        override fun areContentsTheSame(oldItemReplyTemplate: ReplyTemplateUiModel, newItemReplyTemplate: ReplyTemplateUiModel): Boolean {
            return oldItemReplyTemplate == newItemReplyTemplate
        }
    }
}