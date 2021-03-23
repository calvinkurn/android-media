package com.tokopedia.gallery.presenter

interface ReviewGalleryPresenterContract {

    fun loadData(productId: Long, page: Int)

    companion object {
        val DEFAULT_IMAGE_REVIEW_ROW_PER_PAGE = 21
    }
}
