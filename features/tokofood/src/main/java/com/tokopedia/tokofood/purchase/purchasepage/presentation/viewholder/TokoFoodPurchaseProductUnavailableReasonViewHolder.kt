package com.tokopedia.tokofood.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseProductUnavailableReasonBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.purchase.renderAlpha

class TokoFoodPurchaseProductUnavailableReasonViewHolder(private val viewBinding: ItemPurchaseProductUnavailableReasonBinding,
                                                         private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_product_unavailable_reason
    }

    override fun bind(element: TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            textDisabledTitle.text = element.reason
            if (element.detail.isNotBlank()) {
                textDisabledSubTitle.text = element.detail
                textDisabledSubTitle.show()
            } else {
                textDisabledSubTitle.gone()
            }

            itemView.renderAlpha(element)
        }
    }

}