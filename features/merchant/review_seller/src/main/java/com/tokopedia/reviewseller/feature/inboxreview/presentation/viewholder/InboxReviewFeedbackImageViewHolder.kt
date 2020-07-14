package com.tokopedia.reviewseller.feature.inboxreview.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.FeedbackInboxReviewListener
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.unifycomponents.ImageUnify

class InboxReviewFeedbackImageViewHolder(
        itemView: View,
        private val feedbackInboxReviewListener: FeedbackInboxReviewListener
): RecyclerView.ViewHolder(itemView) {

    private val ivItemFeedback: ImageUnify = itemView.findViewById(R.id.ivAttachmentFeedback)

    private var attachmentUiModel: List<FeedbackUiModel.Attachment>? = null
    private var feedbackId = ""

    fun bind(data: FeedbackInboxUiModel.Attachment) {
        ivItemFeedback.setImageUrl(data.thumbnailURL)

        val imageUrls = SellerReviewProductDetailMapper.mapToItemImageSlider(attachmentUiModel)

        itemView.setOnClickListener {
            feedbackInboxReviewListener.onImageItemClicked(imageUrls.first, imageUrls.second, feedbackId, adapterPosition)
        }
    }

    fun setAttachmentUiData(attachmentUiModel: List<FeedbackUiModel.Attachment>?) {
        this.attachmentUiModel = attachmentUiModel
    }

    fun setFeedbackId(feedbackId: String) {
        this.feedbackId = feedbackId
    }
}