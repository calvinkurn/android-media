package com.tokopedia.tokofood.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseAccordionBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.purchase.renderAlpha

class TokoFoodPurchaseAccordionViewHolder(private val viewBinding: ItemPurchaseAccordionBinding,
                                          private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_accordion
    }

    override fun bind(element: TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel) {
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