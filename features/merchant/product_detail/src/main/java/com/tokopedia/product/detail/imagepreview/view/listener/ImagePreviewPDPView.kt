package com.tokopedia.product.detail.imagepreview.view.listener

interface ImagePreviewPDPView {
    fun showLoading()
    fun hideLoading()

    fun gotoLogin()

    fun addWishlist()
    fun removeWishlist()

    fun onSuccessAddWishlist()
    fun onSuccessRemoveWishlist()
    fun onErrorAddWishlist(throwable: Throwable)
    fun onErrorRemoveWishlist(throwable: Throwable)
    fun showMessage(message: String)
    fun showErrorMessage(message: String)
}