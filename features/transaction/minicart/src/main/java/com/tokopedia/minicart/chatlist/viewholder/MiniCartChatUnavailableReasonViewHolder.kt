package com.tokopedia.minicart.chatlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatUnavailableReasonUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartChatUnavailableReasonBinding

class MiniCartChatUnavailableReasonViewHolder(private val viewBinding: ItemMiniCartChatUnavailableReasonBinding) : AbstractViewHolder<MiniCartChatUnavailableReasonUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_chat_unavailable_reason
    }

    override fun bind(element: MiniCartChatUnavailableReasonUiModel) {
        with(viewBinding) {
            tpDesc.text = element.reason
        }
    }
}
