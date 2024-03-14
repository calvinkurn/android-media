package com.tokopedia.topads.sdk.utils

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

internal object ApplyItemDecorationReimagineHelper {

    fun RecyclerView.addItemDecoratorShopAdsReimagine() {
        removeItemDecorationIfExist()
        addItemDecoration(com.tokopedia.topads.sdk.utils.ItemDecorationShopAdsReimagine(6.toPx()))
    }

    fun RecyclerView.addItemDecoratorShopCardAdsReimagine() {
        removeItemDecorationIfExist()
        addItemDecoration(com.tokopedia.topads.sdk.utils.ItemDecorationShopAdsReimagine(0.toPx()))
    }

    private fun RecyclerView.removeItemDecorationIfExist() {
        if (itemDecorationCount > 0) removeItemDecorationAt(0)
    }
}
