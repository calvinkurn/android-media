package com.tokopedia.minicart.cartlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableHeaderUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartUnavailableHeaderBinding

class MiniCartUnavailableHeaderViewHolder(private val viewBinding: ItemMiniCartUnavailableHeaderBinding,
                                          private val listener: MiniCartListActionListener)
    : AbstractViewHolder<MiniCartUnavailableHeaderUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_unavailable_header
    }

    override fun bind(element: MiniCartUnavailableHeaderUiModel) {
        with(viewBinding) {
            val message = textDisabledItemCount.context?.getString(R.string.mini_cart_title_unavailable_header, element.unavailableItemCount)
                    ?: ""
            textDisabledItemCount.text = message
            textDelete.setOnClickListener {
                listener.onBulkDeleteUnavailableItems()
            }
        }
    }

}