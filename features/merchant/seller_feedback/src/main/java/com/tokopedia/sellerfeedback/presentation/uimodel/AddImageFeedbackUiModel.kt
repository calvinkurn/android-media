package com.tokopedia.sellerfeedback.presentation.uimodel

import androidx.annotation.StringRes

data class AddImageFeedbackUiModel(
        @StringRes
        val defaultTitle: Int = 0
) : BaseImageFeedbackUiModel