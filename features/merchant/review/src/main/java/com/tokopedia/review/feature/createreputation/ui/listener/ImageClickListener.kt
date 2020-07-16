package com.tokopedia.review.feature.createreputation.ui.listener

import com.tokopedia.review.feature.createreputation.model.BaseImageReviewViewModel

interface ImageClickListener {
    fun onAddImageClick()
    fun onRemoveImageClick(item: BaseImageReviewViewModel)
}