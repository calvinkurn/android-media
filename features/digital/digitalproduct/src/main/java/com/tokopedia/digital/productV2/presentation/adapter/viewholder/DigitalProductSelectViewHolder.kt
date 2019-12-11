package com.tokopedia.digital.productV2.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.data.product.CatalogProductData
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.digital.productV2.model.DigitalProductItemData
import com.tokopedia.digital.productV2.presentation.model.DigitalProductSelectDropdownData

class DigitalProductSelectViewHolder(val view: View, val listener: OnInputListener) : AbstractViewHolder<DigitalProductItemData>(view) {

    override fun bind(data: DigitalProductItemData) {
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

    private fun mapProducts(data: List<CatalogProductData.DataCollection>): List<DigitalProductSelectDropdownData> {
        val productList = mutableListOf<DigitalProductSelectDropdownData>()
        for (dataCollection in data) {
            for (product in dataCollection.products) {
                with (product.attributes) {
                    val label = productLabels.joinToString(",")
                    val dropdownData = if (promo != null) {
                        DigitalProductSelectDropdownData(product.id, desc, detailCompact, promo!!.newPrice, price, label)
                    } else {
                        DigitalProductSelectDropdownData(product.id, desc, detailCompact, price, label)
                    }
                    productList.add(dropdownData)
                }
            }
        }
        return productList
    }

}