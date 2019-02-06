package com.tokopedia.gallery.customview

import com.tokopedia.gallery.viewmodel.ImageReviewItem

interface ImageReviewSliderView {
    fun displayImage(position: Int)
    fun onLoadDataSuccess(imageReviewItems: List<ImageReviewItem>, isHasNextPage: Boolean)
    fun onLoadDataFailed()
    fun onBackPressed(): Boolean
    fun resetState()
    fun onLoadingData()
}
