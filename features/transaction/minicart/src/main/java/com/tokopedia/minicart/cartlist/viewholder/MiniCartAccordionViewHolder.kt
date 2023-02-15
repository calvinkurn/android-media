package com.tokopedia.minicart.cartlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartAccordionUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartAccordionBinding

class MiniCartAccordionViewHolder(
    private val viewBinding: ItemMiniCartAccordionBinding,
    private val listener: MiniCartListActionListener
) :
    AbstractViewHolder<MiniCartAccordionUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_accordion

        private const val CHEVRON_ROTATION_0 = 0f
        private const val CHEVRON_ROTATION_180 = 180f
    }

    override fun bind(element: MiniCartAccordionUiModel) {
        with(viewBinding) {
            if (element.isCollapsed) {
                textAccordion.text = element.showMoreWording
                imageChevron.rotation = CHEVRON_ROTATION_0
            } else {
                textAccordion.text = element.showLessWording
                imageChevron.rotation = CHEVRON_ROTATION_180
            }
            itemView.setOnClickListener {
                listener.onToggleShowHideUnavailableItemsClicked()
            }
        }
    }
}
