package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListLoadingMoreUiModel

class ShoppingListLoadingMoreViewHolder(itemView: View): AbstractViewHolder<ShoppingListLoadingMoreUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_loading_more
    }

    override fun bind(element: ShoppingListLoadingMoreUiModel) { /* do nothing */ }
}
