package com.tokopedia.developer_options.presentation.feedbackpage.listener

import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.BaseImageFeedbackUiModel

interface ImageClickListener {
    fun addImageClick()
    fun onRemoveImageClick(item: BaseImageFeedbackUiModel)
    fun onImageClick()
}