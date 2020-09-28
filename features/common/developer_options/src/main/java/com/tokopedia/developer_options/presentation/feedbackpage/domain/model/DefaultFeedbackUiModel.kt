package com.tokopedia.developer_options.presentation.feedbackpage.domain.model

import androidx.annotation.StringRes

data class DefaultFeedbackUiModel (
    @StringRes
    val defaultTitle: Int = 0
) : BaseImageFeedbackUiModel