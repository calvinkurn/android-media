package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.spesification.SpecificationBodyDataModel

class ProductSpecificationBodyViewHolder(view: View) : AbstractViewHolder<SpecificationBodyDataModel>(view) {

    private val keyTxt: TextView = view.findViewById(R.id.key_specification)
    private val valueTxt: TextView = view.findViewById(R.id.value_specification)
    override fun bind(element: SpecificationBodyDataModel) {
        keyTxt.text = element.title
        valueTxt.text = element.description
    }
}