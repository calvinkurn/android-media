package com.tokopedia.developer_options.presentation.feedbackpage.viewholder

import android.view.View
import com.tokopedia.developer_options.presentation.feedbackpage.listener.ImageClickListener
import com.tokopedia.developer_options.presentation.feedbackpage.model.DefaultFeedbackUiModel

class DefaultImafeFeedbackViewHolder(val view: View, private val imageClickListener: ImageClickListener): BaseImageFeedbackViewHolder<DefaultFeedbackUiModel>(view) {

    override fun bind(element: DefaultFeedbackUiModel) {
        view.setOnClickListener {
            imageClickListener.addImageClick()
        }
    }

}