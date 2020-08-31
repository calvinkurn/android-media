package com.tokopedia.review.feature.reviewreply.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.review.R
import com.tokopedia.review.common.util.AdapterDiffCallback
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.review.feature.reviewreply.view.viewholder.ReviewReplyFeedbackImageViewHolder

class ReviewReplyFeedbackImageAdapter(private val reviewReplyListener: ReviewReplyListener): ListAdapter<FeedbackUiModel.Attachment,
        ReviewReplyFeedbackImageViewHolder>(AdapterDiffCallback.ImageReviewDiffCallback) {

    private var attachmentUiModel: List<FeedbackUiModel.Attachment>? = null
    private var feedbackId = ""
    private var productTitle = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewReplyFeedbackImageViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_attachment_feedback, parent, false)
        return ReviewReplyFeedbackImageViewHolder(view, reviewReplyListener)
    }

    override fun onBindViewHolder(holderReply: ReviewReplyFeedbackImageViewHolder, position: Int) {
        getItem(position)?.let {
            holderReply.setAttachmentUiData(attachmentUiModel)
            holderReply.setFeedbackId(feedbackId)
            holderReply.setProductTitle(productTitle)
            holderReply.bind(it)
        }
    }

    fun setAttachmentUiData(attachmentUiModel: List<FeedbackUiModel.Attachment>?) {
        this.attachmentUiModel = attachmentUiModel
    }

    fun setFeedbackId(feedbackId: String) {
        this.feedbackId = feedbackId
    }

    fun setProductTitle(productTitle: String) {
        this.productTitle = productTitle
    }

}