package com.tokopedia.tokofood.purchase.purchasepage.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseAddressBinding
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseAddressUiModel

class TokoFoodPurchaseAddressViewHolder(private val viewBinding: ItemPurchaseAddressBinding,
                                        private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseAddressUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_address
    }

    override fun bind(element: TokoFoodPurchaseAddressUiModel) {
        with(viewBinding) {

        }
    }

}