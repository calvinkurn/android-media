package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.unifyprinciples.Typography

class OwocPartialProductItemViewHolder(
    itemView: View?,
    partialProductItemViewStub: View?,
    private var element: OwocProductListUiModel.ProductUiModel
) {

    private val tvOwocProductName =
        partialProductItemViewStub?.findViewById<Typography>(R.id.tvOwocProductName)
    private val tvOwocProductPriceQuantity =
        partialProductItemViewStub?.findViewById<Typography>(R.id.tvOwocProductPriceQuantity)

    private val context = itemView?.context

    init {
        setupProductName(element.productName)
        setupProductQuantityAndPrice(element.quantity, element.priceText)
    }

    fun bindProductItemPayload(
        oldItem: OwocProductListUiModel.ProductUiModel,
        newItem: OwocProductListUiModel.ProductUiModel
    ) {
        this.element = newItem

        if (oldItem.productName != newItem.productName) {
            setupProductName(newItem.productName)
        }
        if (oldItem.quantity != newItem.quantity || oldItem.priceText != newItem.priceText) {
            setupProductQuantityAndPrice(newItem.quantity, newItem.priceText)
        }
    }

    private fun setupProductName(productName: String) {
        tvOwocProductName?.text = productName
    }

    private fun setupProductQuantityAndPrice(quantity: Int, priceText: String) {
        tvOwocProductPriceQuantity?.text =
            context?.getString(R.string.label_product_price_and_quantity, quantity, priceText)
                .orEmpty()
    }
}
