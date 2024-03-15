package com.tokopedia.topads.sdk.utils

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

internal object ApplyItemDecorationReimagineHelper {

    fun RecyclerView.addItemDecoratorShopAdsReimagine() {
        removeItemDecorationIfExist()
        addItemDecoration(ItemDecorationShopAdsReimagine(6.toPx()))
    }

    fun RecyclerView.addItemDecoratorShopCardAdsReimagine() {
        removeItemDecorationIfExist()
        addItemDecoration(ItemDecorationShopAdsReimagine(0.toPx()))
    }

    private fun RecyclerView.removeItemDecorationIfExist() {
        if (itemDecorationCount > 0) removeItemDecorationAt(0)
    }
}
