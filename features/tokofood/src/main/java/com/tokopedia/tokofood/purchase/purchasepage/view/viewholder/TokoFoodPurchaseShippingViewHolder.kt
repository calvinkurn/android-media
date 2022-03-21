package com.tokopedia.tokofood.purchase.purchasepage.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseShippingBinding
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseShippingUiModel

class TokoFoodPurchaseShippingViewHolder(private val viewBinding: ItemPurchaseShippingBinding,
                                         private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseShippingUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_shipping
    }

    override fun bind(element: TokoFoodPurchaseShippingUiModel) {
        with(viewBinding) {

        }
    }

}