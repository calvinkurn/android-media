package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListRetryBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListRetryUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShoppingListRetryViewHolder(
    itemView: View,
    private val listener: ShoppingListRetryListener? = null
): AbstractViewHolder<ShoppingListRetryUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_shopping_list_retry
    }

    private var binding: ItemTokopedianowShoppingListRetryBinding? by viewBinding()

    override fun bind(element: ShoppingListRetryUiModel) {
        binding?.root?.setOnClickListener {
            listener?.onClickRetry()
        }
    }

    interface ShoppingListRetryListener {
        fun onClickRetry()
    }
}
