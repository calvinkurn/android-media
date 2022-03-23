package com.tokopedia.tokofood.purchase.purchasepage.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseAccordionBinding
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseAccordionUiModel
import com.tokopedia.tokofood.purchase.renderAlpha

class TokoFoodPurchaseAccordionViewHolder(private val viewBinding: ItemPurchaseAccordionBinding,
                                          private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseAccordionUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_accordion
    }

    override fun bind(element: TokoFoodPurchaseAccordionUiModel) {
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

            itemView.renderAlpha(element)
        }
    }

}