package com.tokopedia.review.feature.createreputation.model

import androidx.annotation.StringRes
import com.tokopedia.review.R

data class DefaultImageReviewUiModel(
        @StringRes
        val defaultTitle: Int = R.string.review_add_photo_txt
) : BaseImageReviewUiModel