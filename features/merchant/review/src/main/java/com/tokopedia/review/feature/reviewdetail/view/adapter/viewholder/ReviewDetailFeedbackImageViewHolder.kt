package com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.review.feature.reviewdetail.view.adapter.ProductFeedbackDetailListener
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.unifycomponents.ImageUnify

class ReviewDetailFeedbackImageViewHolder(
        itemView: View,
        private val productFeedbackDetailListener: ProductFeedbackDetailListener): RecyclerView.ViewHolder(itemView) {

    private val ivItemFeedback: ImageUnify = itemView.findViewById(R.id.ivAttachmentFeedback)

    private var attachmentUiModel: List<FeedbackUiModel.Attachment>? = null
    private var feedbackId = ""

    fun bind(data: FeedbackUiModel.Attachment) {
        ivItemFeedback.loadImage(data.thumbnailURL.orEmpty())

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