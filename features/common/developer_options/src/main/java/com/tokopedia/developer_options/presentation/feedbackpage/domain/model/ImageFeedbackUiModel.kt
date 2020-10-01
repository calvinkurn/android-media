package com.tokopedia.developer_options.presentation.feedbackpage.domain.model

data class ImageFeedbackUiModel(
        var imageUrl:String = "",
        var otherImageCount : Int = DEFAULT_OTHER_COUNT,
        var shouldDisplayOverlay: Boolean = false
) : BaseImageFeedbackUiModel {
    companion object {
        const val DEFAULT_OTHER_COUNT = 1
    }
}
