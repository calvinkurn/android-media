package com.tokopedia.minicart.chatlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.minicart.R
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatSeparatorUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartChatSeparatorBinding

class MiniCartChatSeparatorViewHolder(private val viewBinding: ItemMiniCartChatSeparatorBinding) :
    AbstractViewHolder<MiniCartChatSeparatorUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_chat_separator
    }

    override fun bind(element: MiniCartChatSeparatorUiModel) {
        itemView.context?.let {
            viewBinding.miniCartSeparator.layoutParams.height = element.height.dpToPx(it.resources.displayMetrics)
            viewBinding.miniCartSeparator.requestLayout()
        }
    }
}
