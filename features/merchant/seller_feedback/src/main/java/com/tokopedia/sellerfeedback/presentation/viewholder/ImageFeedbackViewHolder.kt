package com.tokopedia.sellerfeedback.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.sellerfeedback.presentation.uimodel.ImageFeedbackUiModel
import kotlinx.android.synthetic.main.item_image_chooser_feedback.view.*

class ImageFeedbackViewHolder(val view: View, private val imageClickListener: ImageClickListener) : BaseImageFeedbackViewHolder<ImageFeedbackUiModel>(view) {

    override fun bind(element: ImageFeedbackUiModel) {
        view.remove_image.setOnClickListener {
            imageClickListener.onClickRemoveImage(element)
        }
        ImageHandler.loadImageRounded(view.context, view.image_review, element.imageUrl, 10f)
    }
}