package com.tokopedia.topads.sdk.utils

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

internal object ApplyItemDecorationReimagineHelper {

    fun RecyclerView.setItemDecorationShopAdsReimagineSearch(isReimagine: Boolean) {
        if (isReimagine)
            addItemDecoratorShopAdsReimagine()
        else
            removeItemDecorationIfExist()
    }

    fun RecyclerView.setItemDecorationShopCardAdsReimagineSearch(isReimagine: Boolean) {
        if (isReimagine)
            addItemDecoratorShopCardAdsReimagine()
        else
            removeItemDecorationIfExist()
    }

    private fun RecyclerView.addItemDecoratorShopAdsReimagine() {
        removeItemDecorationIfExist()
        addItemDecoration(ItemDecorationShopAdsReimagine(16.toPx()))
    }

    private fun RecyclerView.addItemDecoratorShopCardAdsReimagine() {
        removeItemDecorationIfExist()
        addItemDecoration(ItemDecorationShopAdsReimagine(0.toPx()))
    }

    private fun RecyclerView.removeItemDecorationIfExist() {
        if (itemDecorationCount > 0) removeItemDecorationAt(0)
    }
}
