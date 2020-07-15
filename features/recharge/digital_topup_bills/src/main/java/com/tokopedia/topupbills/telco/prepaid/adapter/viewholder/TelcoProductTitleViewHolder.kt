package com.tokopedia.topupbills.telco.prepaid.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoCatalogDataCollection
import kotlinx.android.synthetic.main.item_telco_product.view.*

class TelcoProductTitleViewHolder(itemView: View) : AbstractViewHolder<TelcoCatalogDataCollection>(itemView) {

    override fun bind(element: TelcoCatalogDataCollection) {
        with(itemView) {
            title_product.text = element.name
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_digital_title_section_product
    }
}
