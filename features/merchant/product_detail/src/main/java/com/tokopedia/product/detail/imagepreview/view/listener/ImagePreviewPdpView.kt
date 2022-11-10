package com.tokopedia.product.detail.imagepreview.view.listener

import android.content.Context
import com.tokopedia.shop.common.data.source.cloud.model.followshop.Toaster

interface ImagePreviewPdpView {
    fun showLoading()
    fun hideLoading()

    fun gotoLogin()

    fun addWishlistV2(context: Context)
    fun removeWishlistV2(context: Context)

    fun onSuccessAddWishlist()
    fun onSuccessRemoveWishlist()
    fun onErrorAddWishlist(throwable: Throwable)
    fun onErrorRemoveWishlist(throwable: Throwable)
    fun showMessage(message: String)
    fun showErrorMessage(message: String)
}