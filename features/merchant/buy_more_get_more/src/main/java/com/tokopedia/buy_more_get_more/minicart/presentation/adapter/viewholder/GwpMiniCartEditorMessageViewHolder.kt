package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemGwpMiniCartEditorMessageBinding
import com.tokopedia.buy_more_get_more.minicart.presentation.model.GwpMiniCartEditorVisitable

/**
 * Created by @ilhamsuaib on 05/12/23.
 */

class GwpMiniCartEditorMessageViewHolder(
    itemView: View
) : AbstractViewHolder<GwpMiniCartEditorVisitable.GiftMessageUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_gwp_mini_cart_editor_message
    }

    private val binding by lazy {
        ItemGwpMiniCartEditorMessageBinding.bind(itemView)
    }

    override fun bind(element: GwpMiniCartEditorVisitable.GiftMessageUiModel) {
        with(binding) {
            tvMiniCartEditorMessage.setMessages(element.messages)
        }
    }
}