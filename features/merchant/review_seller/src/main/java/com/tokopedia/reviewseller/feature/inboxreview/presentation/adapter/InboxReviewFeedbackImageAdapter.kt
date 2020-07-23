package com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.AdapterDiffCallback
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.viewholder.InboxReviewFeedbackImageViewHolder

class InboxReviewFeedbackImageAdapter(
        private val feedbackInboxReviewListener: FeedbackInboxReviewListener
): ListAdapter<FeedbackInboxUiModel.Attachment,
        InboxReviewFeedbackImageViewHolder>(AdapterDiffCallback.ImageInboxReviewDiffCallback) {

    private var attachmentUiModel: List<FeedbackInboxUiModel.Attachment>? = null
    private var feedbackId = ""
    private var titleProduct = ""
    private var productId = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxReviewFeedbackImageViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_attachment_feedback, parent, false)
        return InboxReviewFeedbackImageViewHolder(view, feedbackInboxReviewListener)
    }

    override fun onBindViewHolder(holder: InboxReviewFeedbackImageViewHolder, position: Int) {
        holder.setAttachmentUiData(attachmentUiModel)
        holder.setFeedbackId(feedbackId)
        holder.setTitleProduct(titleProduct)
        holder.setProductId(productId)
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    fun setAttachmentUiData(attachmentUiModel: List<FeedbackInboxUiModel.Attachment>) {
        this.attachmentUiModel = attachmentUiModel
    }

    fun setFeedbackId(feedbackId: String) {
        this.feedbackId = feedbackId
    }

    fun setTitleProduct(title: String) {
        this.titleProduct = title
    }

    fun setProductId(productId: String) {
        this.titleProduct = productId
    }
}