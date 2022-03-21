package com.tokopedia.tokofood.purchase.purchasepage.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseAccordionBinding
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseAccordionUiModel

class TokoFoodPurchaseAccordionViewHolder(private val viewBinding: ItemPurchaseAccordionBinding,
                                          private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseAccordionUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_accordion
    }

    override fun bind(element: TokoFoodPurchaseAccordionUiModel) {
        with(viewBinding) {

        }
    }

}