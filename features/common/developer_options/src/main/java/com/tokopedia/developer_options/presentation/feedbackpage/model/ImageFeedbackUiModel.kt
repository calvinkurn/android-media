package com.tokopedia.developer_options.presentation.feedbackpage.model

data class ImageFeedbackUiModel(
        val imageUrl:String = "",
        val fullImageUrl: String = "",
        val shouldDisplayOverlay: Boolean = false
) : BaseImageFeedbackUiModel
