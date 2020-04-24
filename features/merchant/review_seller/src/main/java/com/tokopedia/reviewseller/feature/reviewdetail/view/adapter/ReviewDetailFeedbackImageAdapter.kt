package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.ReviewDetailFeedbackImageViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel

class ReviewDetailFeedbackImageAdapter: ListAdapter<FeedbackUiModel.Attachment,
        ReviewDetailFeedbackImageViewHolder>(AdapterDiffCallback.ImageReviewDiffCallback) {

    private var attachmentUiModel: List<FeedbackUiModel.Attachment>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewDetailFeedbackImageViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_product_feedback_detail, parent, false)
        return ReviewDetailFeedbackImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewDetailFeedbackImageViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    fun setImageAttachmentFeedbackData(attachment: List<FeedbackUiModel.Attachment>) {
        this.attachmentUiModel = attachment
    }
}