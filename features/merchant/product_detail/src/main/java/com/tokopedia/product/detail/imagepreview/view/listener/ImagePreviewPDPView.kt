package com.tokopedia.product.detail.imagepreview.view.listener

interface ImagePreviewPDPView {
    fun showLoadin()
    fun hideLoading()

    fun gotoLogin()

    fun addWishlist()
    fun removeWishlist()

    fun onSuccessAddWishlist()
    fun onSuccessRemoveWishlist()
    fun onErrorAddWishlist(throwable: Throwable)
    fun showMessage(message: String)
}