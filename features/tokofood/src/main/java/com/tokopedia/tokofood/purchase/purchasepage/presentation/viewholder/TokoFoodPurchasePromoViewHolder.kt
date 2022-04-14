package com.tokopedia.tokofood.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchasePromoBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchasePromoTokoFoodPurchaseUiModel

class TokoFoodPurchasePromoViewHolder(private val viewBinding: ItemPurchasePromoBinding,
                                      private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchasePromoTokoFoodPurchaseUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_promo
    }

    override fun bind(element: TokoFoodPurchasePromoTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            usePromoAppliedButton.run {
                state = element.state
                title.text = element.title
                description.text = element.description
            }
            itemView.setOnClickListener {
                listener.onPromoWidgetClicked()
            }
        }
    }

}