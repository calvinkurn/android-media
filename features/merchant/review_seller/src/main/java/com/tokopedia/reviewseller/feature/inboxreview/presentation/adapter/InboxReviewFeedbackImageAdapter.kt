package com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.AdapterDiffCallback
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.ReviewDetailFeedbackImageViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel

class InboxReviewFeedbackImageAdapter(
        private val feedbackInboxReviewListener: FeedbackInboxReviewListener
): ListAdapter<FeedbackInboxUiModel.Attachment,
        ReviewDetailFeedbackImageViewHolder>(AdapterDiffCallback.ImageReviewDiffCallback) {

    private var attachmentUiModel: List<FeedbackUiModel.Attachment>? = null
    private var feedbackId = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewDetailFeedbackImageViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_attachment_feedback, parent, false)
        return ReviewDetailFeedbackImageViewHolder(view, feedbackInboxReviewListener)
    }

    override fun onBindViewHolder(holder: ReviewDetailFeedbackImageViewHolder, position: Int) {
        holder.setAttachmentUiData(attachmentUiModel)
        holder.setFeedbackId(feedbackId)
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    fun setAttachmentUiData(attachmentUiModel: List<FeedbackUiModel.Attachment>) {
        this.attachmentUiModel = attachmentUiModel
    }

    fun setFeedbackId(feedbackId: String) {
        this.feedbackId = feedbackId
    }
}