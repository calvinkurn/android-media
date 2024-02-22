package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListTopCheckAllBinding
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListTopCheckAllUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShoppingListTopCheckAllViewHolder(
    itemView: View,
    private val listener: ShoppingListTopCheckAllListener? = null
): AbstractViewHolder<ShoppingListTopCheckAllUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_shopping_list_top_check_all
    }

    private var binding: ItemTokopedianowShoppingListTopCheckAllBinding? by viewBinding()

    override fun bind(
        data: ShoppingListTopCheckAllUiModel
    ) {
        binding?.apply {
            cbAddAll.setOnCheckedChangeListener(null)
            cbAddAll.isChecked = data.isSelected
            cbAddAll.setOnCheckedChangeListener { _, isSelected ->
                listener?.onSelectCheckbox(data.productState, isSelected)
            }
        }
    }

    interface ShoppingListTopCheckAllListener {
        fun onSelectCheckbox(
            productState: ShoppingListProductState,
            isSelected: Boolean
        )
    }
}
