package com.tokopedia.developer_options.presentation.feedbackpage.domain.model

import androidx.annotation.StringRes
import com.tokopedia.developer_options.R

data class DefaultFeedbackUiModel (
    @StringRes
    val defaultTitle: Int = R.string.review_add_photo_txt
) : BaseImageFeedbackUiModel