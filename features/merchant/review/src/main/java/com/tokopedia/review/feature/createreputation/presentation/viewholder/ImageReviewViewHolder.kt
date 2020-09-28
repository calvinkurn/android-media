package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.review.feature.createreputation.model.ImageReviewUiModel
import com.tokopedia.review.feature.createreputation.presentation.listener.ImageClickListener
import kotlinx.android.synthetic.main.item_image_chooser_review.view.*

class ImageReviewViewHolder(val view: View, private val imageClickListener: ImageClickListener?) : BaseImageReviewViewHolder<ImageReviewUiModel>(view) {
    override fun bind(element: ImageReviewUiModel) {
        view.setOnClickListener {
            imageClickListener?.onAddImageClick()
        }
        view.createReviewRemoveImage.setOnClickListener {
            imageClickListener?.onRemoveImageClick(element)
        }
        ImageHandler.loadImageRounded(view.context, view.image_review, element.imageUrl, 10F)
    }

}