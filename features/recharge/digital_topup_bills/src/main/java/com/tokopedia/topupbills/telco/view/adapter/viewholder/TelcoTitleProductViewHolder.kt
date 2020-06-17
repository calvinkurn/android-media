package com.tokopedia.topupbills.telco.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.view.adapter.DigitalProductAdapter

class TelcoTitleProductViewHolder(itemView: View, listener: DigitalProductAdapter.ActionListener)
    : BaseTelcoProductViewHolder(itemView, listener) {

    private val titleProduct: TextView = itemView.findViewById(R.id.title_product)

    override fun bindView(products: List<TelcoProduct>, item: TelcoProduct) {
        super.bindView(products, item)
        titleProduct.text = item.titleSection
    }

    companion object {
        val LAYOUT = R.layout.item_digital_title_section_product
    }
}