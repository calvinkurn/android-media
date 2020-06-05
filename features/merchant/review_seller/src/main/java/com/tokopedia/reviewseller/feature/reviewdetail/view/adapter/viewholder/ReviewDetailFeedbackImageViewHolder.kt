package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.ProductFeedbackDetailListener
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.unifycomponents.ImageUnify

class ReviewDetailFeedbackImageViewHolder(
        itemView: View,
        private val productFeedbackDetailListener: ProductFeedbackDetailListener): RecyclerView.ViewHolder(itemView) {

    private val ivItemFeedback: ImageUnify = itemView.findViewById(R.id.ivAttachmentFeedback)

    private var attachmentUiModel: List<FeedbackUiModel.Attachment>? = null
    private var feedbackId = ""

    fun bind(data: FeedbackUiModel.Attachment) {
        ivItemFeedback.setImageUrl(data.thumbnailURL.orEmpty())

        val imageUrls = SellerReviewProductDetailMapper.mapToItemImageSlider(attachmentUiModel)

        itemView.setOnClickListener {
            productFeedbackDetailListener.onImageItemClicked(imageUrls.first, imageUrls.second, feedbackId, adapterPosition)
        }
    }

    fun setAttachmentUiData(attachmentUiModel: List<FeedbackUiModel.Attachment>?) {
        this.attachmentUiModel = attachmentUiModel
    }

    fun setFeedbackId(feedbackId: String) {
        this.feedbackId = feedbackId
    }
}