package com.tokopedia.rechargegeneral.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.data.product.CatalogProductInput
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductItemData
import com.tokopedia.rechargegeneral.presentation.model.RechargeGeneralProductSelectData

class RechargeGeneralProductSelectViewHolder(val view: View, val listener: OnInputListener) : AbstractViewHolder<RechargeGeneralProductItemData>(view) {

    override fun bind(data: RechargeGeneralProductItemData) {
        val inputView = itemView as TopupBillsInputFieldWidget
        inputView.resetState()
        inputView.isCustomInput = true
        inputView.setLabel(data.text)
        inputView.setHint("")
        inputView.actionListener = object : TopupBillsInputFieldWidget.ActionListener {
            override fun onFinishInput(input: String) {

            }

            override fun onCustomInputClick() {
                listener.onCustomInputClick(inputView, productData = mapProducts(data.dataCollections))
            }

            override fun onTextChangeInput() {
                //do nothing
            }
        }

        // Set recent/applink item data
        if (data.selectedProductId.isNotEmpty()) {
            val product = getProductFromId(data.dataCollections, data.selectedProductId)
            product?.let { inputView.setInputText(it.attributes.desc) }
        }
    }

    private fun mapProducts(data: List<CatalogProductInput.DataCollection>): List<RechargeGeneralProductSelectData> {
        val productList = mutableListOf<RechargeGeneralProductSelectData>()
        for (dataCollection in data) {
            for (product in dataCollection.products) {
                with(product.attributes) {
                    val label = productLabels.joinToString(",")
                    val slashedPrice = if (promo != null) price else ""

                    val dropdownData = RechargeGeneralProductSelectData(
                            product.id,
                            desc,
                            detailCompact,
                            promo?.newPrice ?: price,
                            slashedPrice,
                            label,
                            promo != null)
                    productList.add(dropdownData)
                }
            }
        }
        return productList
    }

    private fun getProductFromId(data: List<CatalogProductInput.DataCollection>, id: String): CatalogProduct? {
        data.forEach { collection ->
            collection.products.forEach { product ->
                if (product.id == id) return product
            }
        }
        return null
    }

}