package com.tokopedia.feedback_form.feedbackpage.domain.model

import androidx.annotation.StringRes
import com.tokopedia.feedback_form.R

data class DefaultFeedbackUiModel (
    @StringRes
    val defaultTitle: Int = R.string.review_add_photo_txt
) : BaseImageFeedbackUiModel