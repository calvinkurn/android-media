package com.tokopedia.topads.sdk.utils

import androidx.recyclerview.widget.RecyclerView

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
        addItemDecoration(ItemDecorationShopAdsReimagine())
    }

    private fun RecyclerView.addItemDecoratorShopCardAdsReimagine() {
        removeItemDecorationIfExist()
        addItemDecoration(ItemDecorationShopCardAdsReimagine())
    }

    private fun RecyclerView.removeItemDecorationIfExist(){
        if (itemDecorationCount > 0) removeItemDecorationAt(0)
    }
}
