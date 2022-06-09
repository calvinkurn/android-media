package com.tokopedia.review.feature.imagepreview.presentation.listener

interface ReviewImagePreviewListener {
    fun onImageClicked()
    fun disableScroll()
    fun enableScroll()
    fun onImageLoadFailed(index: Int, throwable: Throwable?)
    fun onImageImpressed()
}