package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.bottomsheet

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.GlobalErrorUtil.setupLayout
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListAnotherOptionErrorStateBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.bottomsheet.ShoppingListAnotherOptionBottomSheetErrorStateUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShoppingListAnotherOptionBottomSheetErrorStateViewHolder (
    itemView: View,
    private val listener: ShoppingListAnotherOptionBottomSheetErrorStateListener? = null
): AbstractViewHolder<ShoppingListAnotherOptionBottomSheetErrorStateUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_shopping_list_another_option_error_state
    }

    private var binding: ItemTokopedianowShoppingListAnotherOptionErrorStateBinding? by viewBinding()

    override fun bind(element: ShoppingListAnotherOptionBottomSheetErrorStateUiModel) {
        binding?.root?.setupLayout(element.throwable) {
            listener?.onClickRefresh()
        }
    }

    interface ShoppingListAnotherOptionBottomSheetErrorStateListener {
        fun onClickRefresh()
    }
}
