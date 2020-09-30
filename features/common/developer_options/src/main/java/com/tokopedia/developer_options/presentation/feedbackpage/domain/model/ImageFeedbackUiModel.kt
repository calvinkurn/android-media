package com.tokopedia.developer_options.presentation.feedbackpage.domain.model

data class ImageFeedbackUiModel(
        val imageUrl:String = "",
        val otherImageCount : Int = DEFAULT_OTHER_COUNT,
        val shouldDisplayOverlay: Boolean = false
) : BaseImageFeedbackUiModel {
    companion object {
        const val DEFAULT_OTHER_COUNT = 1
    }
}
