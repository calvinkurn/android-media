package com.tokopedia.review.feature.inboxreview.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.feature.inboxreview.domain.mapper.InboxReviewMapper
import com.tokopedia.review.feature.inboxreview.presentation.adapter.FeedbackInboxReviewListener
import com.tokopedia.review.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.unifycomponents.ImageUnify

class InboxReviewFeedbackImageViewHolder(
        itemView: View,
        private val feedbackInboxReviewListener: FeedbackInboxReviewListener
): RecyclerView.ViewHolder(itemView) {

    private val ivItemFeedback: ImageUnify = itemView.findViewById(R.id.ivAttachmentFeedback)

    private var attachmentUiModel: List<FeedbackInboxUiModel.Attachment>? = null
    private var feedbackId = ""
    private var titleProduct = ""
    private var productId = ""

    fun bind(data: FeedbackInboxUiModel.Attachment) {
        ivItemFeedback.loadImage(data.thumbnailURL)

        val imageUrls = InboxReviewMapper.mapToItemImageSlider(attachmentUiModel)

        itemView.setOnClickListener {
            feedbackInboxReviewListener.onImageItemClicked(titleProduct, imageUrls.first, imageUrls.second, feedbackId, productId, adapterPosition)
        }
    }

    fun setAttachmentUiData(attachmentUiModel: List<FeedbackInboxUiModel.Attachment>?) {
        this.attachmentUiModel = attachmentUiModel
    }

    fun setFeedbackId(feedbackId: String) {
        this.feedbackId = feedbackId
    }

    fun setTitleProduct(title: String) {
        this.titleProduct = title
    }

    fun setProductId(productId: String) {
        this.productId = productId
    }
}