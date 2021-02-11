package com.tokopedia.feedback_form.feedbackpage.ui.listener

import com.tokopedia.feedback_form.feedbackpage.domain.model.BaseImageFeedbackUiModel
import com.tokopedia.feedback_form.feedbackpage.domain.model.ImageFeedbackUiModel

interface ImageClickListener {
    fun addImageClick()
    fun onRemoveImageClick(item: BaseImageFeedbackUiModel)
    fun onImageClick(data: ImageFeedbackUiModel, position: Int)
}