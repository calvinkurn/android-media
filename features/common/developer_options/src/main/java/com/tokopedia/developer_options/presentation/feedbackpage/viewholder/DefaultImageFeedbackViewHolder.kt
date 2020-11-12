package com.tokopedia.developer_options.presentation.feedbackpage.viewholder

import android.view.View
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.DefaultFeedbackUiModel
import com.tokopedia.developer_options.presentation.feedbackpage.listener.ImageClickListener

class DefaultImageFeedbackViewHolder(val view: View, private val imageClickListener: ImageClickListener): BaseImageFeedbackViewHolder<DefaultFeedbackUiModel>(view) {

    override fun bind(element: DefaultFeedbackUiModel) {
        view.setOnClickListener {
            imageClickListener.addImageClick()
        }
    }

}