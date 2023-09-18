package com.tokopedia.topads.sdk.utils

import androidx.recyclerview.widget.RecyclerView

internal object ApplyItemDecorationReimagineHelper {

    fun RecyclerView.setItemDecorationReimagineSearch(isReimagine: Boolean) {
        if (isReimagine)
            addItemDecoratorReimagine()
        else
            removeItemDecorationIfExist()
    }

    private fun RecyclerView.addItemDecoratorReimagine() {
        removeItemDecorationIfExist()
        addItemDecoration(ItemDecorationReimagine())
    }

    private fun RecyclerView.removeItemDecorationIfExist(){
        if (itemDecorationCount > 0) removeItemDecorationAt(0)
    }
}
