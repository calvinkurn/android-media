package com.tokopedia.gallery.customview

interface BottomSheetImageReviewSliderCallback {
    val isAllowLoadMore: Boolean
    fun onRequestLoadMore(page: Int)
    fun onButtonBackPressed()
    fun onSeeAllButtonClicked()
}