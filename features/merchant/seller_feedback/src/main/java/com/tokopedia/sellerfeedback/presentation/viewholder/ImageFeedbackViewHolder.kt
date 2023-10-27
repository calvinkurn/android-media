package com.tokopedia.sellerfeedback.presentation.viewholder

import android.view.View
import com.tokopedia.sellerfeedback.databinding.ItemImageChooserFeedbackBinding
import com.tokopedia.sellerfeedback.presentation.uimodel.ImageFeedbackUiModel
import com.tokopedia.media.loader.loadImageRounded

class ImageFeedbackViewHolder(
    view: View,
    private val imageClickListener: ImageClickListener
) : BaseImageFeedbackViewHolder<ImageFeedbackUiModel>(view) {

    companion object {
        private const val IMAGE_ROUND_RADIUS = 10f
    }

    private val binding = ItemImageChooserFeedbackBinding.bind(view)

    override fun bind(element: ImageFeedbackUiModel) {
        val context = binding.root.context
        binding.removeImage.setOnClickListener {
            imageClickListener.onClickRemoveImage(element)
        }
        binding.imageReview.loadImageRounded(element.imageUrl, IMAGE_ROUND_RADIUS)
    }
}
