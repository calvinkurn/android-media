package com.tokopedia.gallery.presenter

import android.content.Context

interface ReviewGalleryPresenter {

    fun cancelLoadDataRequest()
    fun loadData(productId: Int, page: Int)

    companion object {
        val DEFAULT_IMAGE_REVIEW_ROW_PER_PAGE = 21
    }
}
