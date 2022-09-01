package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseProductUnavailableReasonBinding
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.renderAlpha

class TokoFoodPurchaseProductUnavailableReasonViewHolder(private val viewBinding: ItemPurchaseProductUnavailableReasonBinding)
    : AbstractViewHolder<TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_product_unavailable_reason
    }

    override fun bind(element: TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            unavailableSectionType.text = element.reason
            itemView.renderAlpha(element)
        }
    }

}