package com.tokopedia.home_recom.view.viewHolder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.model.dataModel.ProductInfoDataModel
import com.tokopedia.home_recom.R

class ProductInfoViewHolder(view: View) : AbstractViewHolder<ProductInfoDataModel>(view) {

    private val productName: TextView by lazy { view.findViewById<TextView>(R.id.product_name) }
    private val productDescription: TextView by lazy { view.findViewById<TextView>(R.id.product_description) }

    override fun bind(element: ProductInfoDataModel) {
        productName.text = element.product.name
        productDescription.text = element.product.name
    }
}