package com.tokopedia.digital.productV2.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.data.product.CatalogProductData
import com.tokopedia.common.topupbills.view.model.TopupBillsInputDropdownData
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.digital.productV2.model.DigitalProductItemData

class DigitalProductSelectViewHolder(val view: View, val listener: OnInputListener) : AbstractViewHolder<DigitalProductItemData>(view) {

    override fun bind(data: DigitalProductItemData) {
        val inputView = itemView as TopupBillsInputFieldWidget
        inputView.resetState()
        inputView.setLabel(data.text)
        inputView.setHint("")
        inputView.setupDropdownBottomSheet(mapProducts(data.dataCollections))
        inputView.setActionListener(object : TopupBillsInputFieldWidget.ActionListener{
            override fun onFinishInput(input: String) {
                listener.onFinishInput(input, adapterPosition)
            }
        })
    }

    private fun mapProducts(data: List<CatalogProductData.DataCollection>): List<TopupBillsInputDropdownData> {
        val productList = mutableListOf<TopupBillsInputDropdownData>()
        for (dataCollection in data) {
            for (product in dataCollection.products) {
                productList.add(TopupBillsInputDropdownData(product.attributes.desc))
            }
        }
        return productList
    }

}