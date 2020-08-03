package com.tokopedia.tkpd.tkpdreputation.createreputation.model

import androidx.annotation.StringRes
import com.tokopedia.tkpd.tkpdreputation.R

data class DefaultImageReviewUiModel(
        @StringRes
        val defaultTitle: Int = R.string.review_add_photo_txt
) : BaseImageReviewUiModel