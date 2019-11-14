package com.tokopedia.similarsearch

import android.view.View
import com.tokopedia.discovery.common.model.SimilarSearchSelectedProduct

internal interface SimilarSearchSelectedProductViewListener {

    fun getSelectedProduct(): SimilarSearchSelectedProduct?

    fun getFragmentView(): View

    fun onButtonWishlistClicked()
}