package com.tokopedia.reviewseller.feature.inboxreview.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.inboxreview.domain.mapper.InboxReviewMapper
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.FeedbackInboxReviewListener
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FeedbackInboxUiModel
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
        ivItemFeedback.setImageUrl(data.thumbnailURL)

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