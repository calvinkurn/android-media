package com.tokopedia.review.feature.createreputation.presentation.listener

import com.tokopedia.review.feature.createreputation.model.BaseImageReviewViewModel

interface ImageClickListener {
    fun onAddImageClick()
    fun onRemoveImageClick(item: BaseImageReviewViewModel)
}