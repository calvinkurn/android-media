package com.tokopedia.review.feature.gallery.presentation.listener

interface ReviewGalleryImageListener {
    fun onImageClicked()
    fun disableScroll()
    fun enableScroll()
    fun onImageLoadFailed(isSuccess: Boolean, index: Int)
}