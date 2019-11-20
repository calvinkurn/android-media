package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductGeneralInfoDataModel
import kotlinx.android.synthetic.main.item_dynamic_general_info.view.*

class ProductGeneralInfoViewHolder(val view: View) : AbstractViewHolder<ProductGeneralInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_general_info
    }

    override fun bind(element: ProductGeneralInfoDataModel) {
        if (element.dataLayout.isNotEmpty()) {
            view.pdp_info_title.text = element.dataLayout.first().title
        }
    }
}