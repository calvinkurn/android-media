package com.tokopedia.review.common.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.common.util.ReviewAttachedImagesClickListener
import com.tokopedia.review.feature.inbox.history.presentation.util.ReviewHistoryItemListener
import com.tokopedia.review.inbox.databinding.ItemReviewAttachedImageBinding

class ReviewAttachedProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemReviewAttachedImageBinding.bind(view)

    fun bind(attachedImageUrl: String, reviewAttachedImagesClickListener: ReviewAttachedImagesClickListener,
             attachedImages: List<String>, productName: String, reviewHistoryItemListener: ReviewHistoryItemListener? = null,
             productId: String? = null,
             feedbackId: String? = null) {
        if(attachedImageUrl.isEmpty()) {
            binding.apply {
                reviewHistoryAttachedImageBlankSpace.show()
                reviewHistoryAttachedImage.hide()
            }
            return
        }
        binding.apply {
            reviewHistoryAttachedImageBlankSpace.hide()
            reviewHistoryAttachedImage.show()
            reviewHistoryAttachedImage.loadImage(attachedImageUrl)
            root.setOnClickListener {
                reviewHistoryItemListener?.trackAttachedImageClicked(productId, feedbackId)
                reviewAttachedImagesClickListener.onAttachedImagesClicked(productName, attachedImages, adapterPosition - 1)
            }
        }
    }
}