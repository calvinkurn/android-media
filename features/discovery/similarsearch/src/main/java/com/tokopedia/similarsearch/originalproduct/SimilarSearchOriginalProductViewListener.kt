package com.tokopedia.similarsearch.originalproduct

import android.view.View

internal interface SimilarSearchOriginalProductViewListener {

    fun getFragmentView(): View

    fun onItemClicked()

    fun onButtonWishlistClicked()

    fun onButtonBuyClicked()

    fun onButtonAddToCartClicked()
}