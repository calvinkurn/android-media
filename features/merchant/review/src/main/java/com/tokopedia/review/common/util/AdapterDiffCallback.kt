package com.tokopedia.review.common.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.review.feature.reviewreply.view.model.ReplyTemplateUiModel

object AdapterDiffCallback {
    val replyTemplateDiffCallback = object: DiffUtil.ItemCallback<ReplyTemplateUiModel>() {
        override fun areItemsTheSame(oldItemReplyTemplate: ReplyTemplateUiModel, newItemReplyTemplate: ReplyTemplateUiModel): Boolean {
            return oldItemReplyTemplate == newItemReplyTemplate
        }

        override fun areContentsTheSame(oldItemReplyTemplate: ReplyTemplateUiModel, newItemReplyTemplate: ReplyTemplateUiModel): Boolean {
            return oldItemReplyTemplate == newItemReplyTemplate
        }
    }
}