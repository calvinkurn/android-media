package com.tokopedia.developer_options.presentation.feedbackpage.listener

import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.BaseImageFeedbackUiModel
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.ImageFeedbackUiModel

interface ImageClickListener {
    fun addImageClick()
    fun onRemoveImageClick(item: BaseImageFeedbackUiModel)
    fun onImageClick(data: ImageFeedbackUiModel, position: Int)
}