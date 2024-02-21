package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListTopCheckAllUiModel

class ShoppingListTopCheckAllViewHolder(
    itemView: View
): AbstractViewHolder<ShoppingListTopCheckAllUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_shopping_list_top_check_all
    }

    override fun bind(element: ShoppingListTopCheckAllUiModel) { /* nothing to do */ }
}
