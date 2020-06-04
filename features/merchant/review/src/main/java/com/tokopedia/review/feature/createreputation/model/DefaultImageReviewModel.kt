package com.tokopedia.review.feature.createreputation.model

import androidx.annotation.StringRes
import com.tokopedia.review.R

data class DefaultImageReviewModel(
        @StringRes
        val defaultTitle: Int = R.string.review_add_photo_txt
) : BaseImageReviewViewModel