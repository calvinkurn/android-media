package com.tokopedia.similarsearch

import android.view.View

internal interface SimilarSearchOriginalProductViewListener {

    fun getFragmentView(): View

    fun onItemClicked()

    fun onButtonWishlistClicked()

    fun onButtonBuyClicked()

    fun onButtonAddToCartClicked()
}