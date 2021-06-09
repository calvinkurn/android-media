package com.tokopedia.review.common.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.common.presentation.util.ReviewAttachedImagesClickListener
import com.tokopedia.review.feature.inbox.history.presentation.util.ReviewHistoryItemListener
import kotlinx.android.synthetic.main.item_review_attached_image.view.*

class ReviewAttachedProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(attachedImageUrl: String, reviewAttachedImagesClickListener: ReviewAttachedImagesClickListener,
             attachedImages: List<String>, productName: String, reviewHistoryItemListener: ReviewHistoryItemListener? = null,
             productId: Long? = null,
             feedbackId: Long? = null) {
        if(attachedImageUrl.isEmpty()) {
            itemView.apply {
                reviewHistoryAttachedImageBlankSpace.show()
                reviewHistoryAttachedImage.hide()
            }
            return
        }
        itemView.apply {
            reviewHistoryAttachedImageBlankSpace.hide()
            reviewHistoryAttachedImage.show()
            reviewHistoryAttachedImage.loadImage(attachedImageUrl)
            setOnClickListener {
                reviewHistoryItemListener?.trackAttachedImageClicked(productId, feedbackId)
                reviewAttachedImagesClickListener.onAttachedImagesClicked(productName, attachedImages, adapterPosition)
            }
        }
    }
}