package com.tokopedia.tokofood.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseProductListHeaderBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.purchase.renderAlpha

class TokoFoodPurchaseProductListHeaderViewHolder(private val viewBinding: ItemPurchaseProductListHeaderBinding,
                                                  private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_product_list_header
    }

    override fun bind(element: TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            textProductListHeader.text = element.title
            textProductListAction.text = element.action
            textProductListAction.setOnClickListener {
                if (!element.isUnavailableHeader) {
                    listener.onTextAddItemClicked()
                } else {
                    listener.onTextBulkDeleteUnavailableProductsClicked()
                }
            }

            textProductListHeader.renderAlpha(element)
            textProductListAction.renderAlpha(element)
        }
    }

}