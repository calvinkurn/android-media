package com.tokopedia.rechargegeneral.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.data.product.CatalogProductData
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
        inputView.setActionListener(object : TopupBillsInputFieldWidget.ActionListener{
            override fun onFinishInput(input: String) {

            }

            override fun onCustomInputClick() {
                listener.onCustomInputClick(inputView, mapProducts(data.dataCollections), adapterPosition)
            }
        })

        // Set recent item data
        if (data.value.isNotEmpty()) inputView.setInputText(data.value)
    }

    private fun mapProducts(data: List<CatalogProductData.DataCollection>): List<RechargeGeneralProductSelectData> {
        val productList = mutableListOf<RechargeGeneralProductSelectData>()
        for (dataCollection in data) {
            for (product in dataCollection.products) {
                with (product.attributes) {
                    val label = productLabels.joinToString(",")
                    val dropdownData = if (promo != null) {
                        RechargeGeneralProductSelectData(product.id, desc, detailCompact, promo!!.newPrice, price, label)
                    } else {
                        RechargeGeneralProductSelectData(product.id, desc, detailCompact, price, label)
                    }
                    productList.add(dropdownData)
                }
            }
        }
        return productList
    }

}