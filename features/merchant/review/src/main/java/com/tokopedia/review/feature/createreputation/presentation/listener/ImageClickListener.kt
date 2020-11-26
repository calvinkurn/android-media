package com.tokopedia.review.feature.createreputation.presentation.listener

import com.tokopedia.review.feature.createreputation.model.BaseImageReviewUiModel

interface ImageClickListener {
    fun onAddImageClick()
    fun onRemoveImageClick(item: BaseImageReviewUiModel)
}