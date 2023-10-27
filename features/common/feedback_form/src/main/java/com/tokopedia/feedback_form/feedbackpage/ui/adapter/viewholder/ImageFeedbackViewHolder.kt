package com.tokopedia.feedback_form.feedbackpage.ui.adapter.viewholder

import android.view.View
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.feedback_form.feedbackpage.domain.model.ImageFeedbackUiModel
import com.tokopedia.feedback_form.feedbackpage.ui.listener.ImageClickListener
import kotlinx.android.synthetic.main.item_image_chooser_feedback.view.*

class ImageFeedbackViewHolder (val view: View, private val imageClickListener: ImageClickListener): BaseImageFeedbackViewHolder<ImageFeedbackUiModel>(view) {

    override fun bind(element: ImageFeedbackUiModel) {
        view.setOnClickListener {
            imageClickListener.onImageClick(element, adapterPosition)
        }
        view.remove_image.setOnClickListener {
            imageClickListener.onRemoveImageClick(element)
        }

        view.image_review?.loadImageRounded(element.imageUrl, 10f)

    }


}