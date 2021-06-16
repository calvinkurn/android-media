package com.tokopedia.sellerfeedback.presentation.viewholder

import android.view.View
import com.tokopedia.sellerfeedback.presentation.uimodel.AddImageFeedbackUiModel

class AddImageFeedbackViewHolder(val view: View, private val imageClickListener: ImageClickListener) : BaseImageFeedbackViewHolder<AddImageFeedbackUiModel>(view) {
    override fun bind(element: AddImageFeedbackUiModel) {
        view.setOnClickListener {
            imageClickListener.onClickAddImage()
        }
    }
}