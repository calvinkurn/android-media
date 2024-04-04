package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.bottomsheet

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.bottomsheet.ShoppingListAnotherOptionBottomSheetEmptyStateUiModel

class ShoppingListAnotherOptionBottomSheetEmptyStateViewHolder (
    itemView: View
): AbstractViewHolder<ShoppingListAnotherOptionBottomSheetEmptyStateUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_shopping_list_another_option_empty_state
    }

    override fun bind(element: ShoppingListAnotherOptionBottomSheetEmptyStateUiModel) { /* do nothing */ }
}
