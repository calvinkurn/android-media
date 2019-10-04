package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.spesification.SpecificationTitleDataModel

class ProductSpecificationTitleViewHolder(view: View) : AbstractViewHolder<SpecificationTitleDataModel>(view) {

    val title: TextView = view.findViewById(R.id.specification_title)
    override fun bind(element: SpecificationTitleDataModel) {
        title.text = element.title
    }

}