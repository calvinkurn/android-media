package com.tokopedia.topupbills.telco.prepaid.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoCatalogDataCollection
import kotlinx.android.synthetic.main.item_telco_title_section_product.view.*

class TelcoProductTitleViewHolder(itemView: View) : AbstractViewHolder<TelcoCatalogDataCollection>(itemView) {

    override fun bind(element: TelcoCatalogDataCollection) {
        with(itemView) {
            if (element.name.isEmpty()) hide()
            else {
                telco_prepaid_title_product.text = element.name
                show()
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_telco_title_section_product
    }
}
