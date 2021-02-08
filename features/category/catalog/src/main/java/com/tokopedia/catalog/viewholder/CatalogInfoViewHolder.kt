package com.tokopedia.catalog.viewholder

import android.view.View
import com.tokopedia.catalog.R
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogInfoDataModel
import kotlinx.android.synthetic.main.item_catalog_product_info.view.*

class CatalogInfoViewHolder (private val view : View,
                             private val listener : CatalogDetailListener) : AbstractViewHolder<CatalogInfoDataModel>(view){

    companion object {
        val LAYOUT = R.layout.item_catalog_product_info
    }

    override fun bind(element: CatalogInfoDataModel) {

        renderProductHeaderInfo(element)
    }

    private fun renderProductHeaderInfo(productInfo: CatalogInfoDataModel) {
        productInfo.run {
            view.product_name.text = productName
            view.product_brand.text = productBrand
            view.product_label.text = tag
            view.price_range_value.text = priceRrange
            view.product_description.text = description
        }
    }
}