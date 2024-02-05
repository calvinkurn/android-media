package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListTopCheckAllBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListTopCheckAllUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShoppingListTopCheckAllViewHolder(
    itemView: View
): AbstractViewHolder<ShoppingListTopCheckAllUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_shopping_list_top_check_all
    }

    private var binding: ItemTokopedianowShoppingListTopCheckAllBinding? by viewBinding()

    override fun bind(element: ShoppingListTopCheckAllUiModel) {
        binding?.apply {
            tpAllPrice.text = element.allPrice
        }
    }

}
