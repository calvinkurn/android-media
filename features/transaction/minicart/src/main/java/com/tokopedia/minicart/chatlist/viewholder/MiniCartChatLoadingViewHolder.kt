package com.tokopedia.minicart.chatlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.minicart.R
import com.tokopedia.minicart.databinding.ItemMiniCartChatLoadingBinding
import com.tokopedia.unifycomponents.toPx

class MiniCartChatLoadingViewHolder(private var viewBinding: ItemMiniCartChatLoadingBinding) :
    LoadingViewholder(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_chat_loading
    }

    override fun bind(element: LoadingModel?) {
        viewBinding.containerProductShimmering.setMargin(16.toPx(), 0, 16.toPx(), 0)
    }
}
