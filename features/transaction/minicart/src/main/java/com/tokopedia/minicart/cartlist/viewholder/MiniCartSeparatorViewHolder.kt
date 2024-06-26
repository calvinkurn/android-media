package com.tokopedia.minicart.cartlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.uimodel.MiniCartSeparatorUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartSeparatorBinding

class MiniCartSeparatorViewHolder(private val viewBinding: ItemMiniCartSeparatorBinding) :
    AbstractViewHolder<MiniCartSeparatorUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_separator
    }

    override fun bind(element: MiniCartSeparatorUiModel) {
        itemView.context?.let {
            viewBinding.miniCartSeparator.layoutParams.height = element.height.dpToPx(it.resources.displayMetrics)
            viewBinding.miniCartSeparator.requestLayout()
        }
    }
}
