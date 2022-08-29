package com.tokopedia.review.feature.createreputation.presentation.viewholder.old

import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.review.databinding.ItemImageChooserReviewBinding
import com.tokopedia.review.feature.createreputation.model.ImageReviewUiModel
import com.tokopedia.review.feature.createreputation.presentation.listener.ImageClickListener

class ImageReviewViewHolder(view: View, private val imageClickListener: ImageClickListener?) :
    BaseImageReviewViewHolder<ImageReviewUiModel>(view) {

    private val binding = ItemImageChooserReviewBinding.bind(view)

    override fun bind(element: ImageReviewUiModel) {
        binding.root.setOnClickListener {
            imageClickListener?.onAddImageClick()
        }
        binding.createReviewRemoveImage.setOnClickListener {
            imageClickListener?.onRemoveImageClick(element)
        }
        ImageHandler.loadImageRounded(binding.root.context, binding.imageReview, element.imageUrl, 10F)
    }

}