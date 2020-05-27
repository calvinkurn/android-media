package com.tokopedia.topupbills.telco.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.view.adapter.DigitalProductAdapter

class TelcoTitleProductViewHolder(itemView: View, listener: DigitalProductAdapter.ActionListener)
    : BaseTelcoProductViewHolder(itemView, listener) {

    override fun bindView(products: List<TelcoProduct>, item: TelcoProduct) {
        super.bindView(products, item)
        titleProduct.text = item.titleSection
    }

    override fun onClickProductItem() {
        //do nothing
    }

    override fun setItemSelected(viewGrup: ViewGroup) {
        //do nothing
    }

    companion object {
        val LAYOUT = R.layout.item_digital_title_section_product
    }
}