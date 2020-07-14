package com.tokopedia.topupbills.telco.prepaid.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topupbills.R
import kotlinx.android.synthetic.main.view_telco_digital_empty_product_list.view.*

class TelcoProductEmptyViewHolder(itemView: View) : AbstractViewHolder<EmptyModel>(itemView) {

    override fun bind(element: EmptyModel?) {
        with(itemView) {
            title_empty_product.text = ""
            desc_empty_product.text = ""
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_telco_digital_empty_product_list
    }

}