package com.tokopedia.gallery

import com.tokopedia.gallery.viewmodel.ImageReviewItem

interface GalleryView {
    fun onGalleryItemClicked(position: Int)
    fun handleItemResult(imageReviewItemList: List<ImageReviewItem>, isHasNextPage: Boolean)
    fun handleErrorResult(e: Throwable)
}
