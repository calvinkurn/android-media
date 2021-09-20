package com.tokopedia.minicart.cartlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartAccordionUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartAccordionBinding

class MiniCartAccordionViewHolder(private val viewBinding: ItemMiniCartAccordionBinding,
                                  private val listener: MiniCartListActionListener)
    : AbstractViewHolder<MiniCartAccordionUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_accordion
    }

    override fun bind(element: MiniCartAccordionUiModel) {
        with(viewBinding) {
            if (element.isCollapsed) {
                textAccordion.text = element.showMoreWording
                imageChevron.rotation = 0f
            } else {
                textAccordion.text = element.showLessWording
                imageChevron.rotation = 180f
            }
            itemView.setOnClickListener {
                listener.onToggleShowHideUnavailableItemsClicked()
            }
        }
    }

}