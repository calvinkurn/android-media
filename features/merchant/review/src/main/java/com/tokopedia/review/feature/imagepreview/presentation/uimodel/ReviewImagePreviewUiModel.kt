package com.tokopedia.review.feature.imagepreview.presentation.uimodel

import com.tokopedia.kotlin.model.ImpressHolder

data class ReviewImagePreviewUiModel(
    val imageUrl: String = "",
    val impressHolder: ImpressHolder = ImpressHolder()
)