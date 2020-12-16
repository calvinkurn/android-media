package com.tokopedia.feedback_form.feedbackpage.ui.adapter.viewholder

import android.view.View
import com.tokopedia.feedback_form.feedbackpage.domain.model.DefaultFeedbackUiModel
import com.tokopedia.feedback_form.feedbackpage.ui.listener.ImageClickListener

class DefaultImageFeedbackViewHolder(val view: View, private val imageClickListener: ImageClickListener): BaseImageFeedbackViewHolder<DefaultFeedbackUiModel>(view) {

    override fun bind(element: DefaultFeedbackUiModel) {
        view.setOnClickListener {
            imageClickListener.addImageClick()
        }
    }

}