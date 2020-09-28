package com.tokopedia.developer_options.presentation.feedbackpage.domain.model

data class ImageFeedbackUiModel(
        val imageUrl:String = "",
        val fullImageUrl: String = "",
        val shouldDisplayOverlay: Boolean = false
) : BaseImageFeedbackUiModel
