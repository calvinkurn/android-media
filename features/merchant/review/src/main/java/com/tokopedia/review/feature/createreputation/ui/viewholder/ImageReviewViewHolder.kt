package com.tokopedia.review.feature.createreputation.ui.viewholder

import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.model.ImageReviewViewModel
import com.tokopedia.review.feature.createreputation.ui.listener.ImageClickListener
import kotlinx.android.synthetic.main.item_image_chooser_review.view.*

class ImageReviewViewHolder(val view: View, private val imageClickListener: ImageClickListener?) : BaseImageReviewViewHolder<ImageReviewViewModel>(view) {
    override fun bind(element: ImageReviewViewModel) {
        view.setOnClickListener {
            imageClickListener?.onAddImageClick()
        }
        if (adapterPosition == 3 && element.shouldDisplayOverlay) {
            view.review_container_overlay.visibility = View.VISIBLE
            view.txt_other_img_overlay.text = view.context.getString(R.string.review_overlay_count, element.otherImageCount)
        } else {
            view.review_container_overlay.visibility = View.GONE
        }

        ImageHandler.loadImageRounded(view.context, view.image_review, element.imageUrl, 10F)
    }

}