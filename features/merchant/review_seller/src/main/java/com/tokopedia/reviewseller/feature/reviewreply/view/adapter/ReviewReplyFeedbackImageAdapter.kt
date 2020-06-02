package com.tokopedia.reviewseller.feature.reviewreply.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.AdapterDiffCallback
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.reviewseller.feature.reviewreply.view.viewholder.ReviewReplyFeedbackImageViewHolder

class ReviewReplyFeedbackImageAdapter: ListAdapter<FeedbackUiModel.Attachment,
        ReviewReplyFeedbackImageViewHolder>(AdapterDiffCallback.ImageReviewDiffCallback) {

    private var attachmentUiModel: List<FeedbackUiModel.Attachment>? = null
    private var feedbackId = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewReplyFeedbackImageViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_attachment_feedback, parent, false)
        return ReviewReplyFeedbackImageViewHolder(view)
    }

    override fun onBindViewHolder(holderReply: ReviewReplyFeedbackImageViewHolder, position: Int) {
        getItem(position)?.let {
            holderReply.bind(it)
        }
    }
}