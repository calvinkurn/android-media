package com.tokopedia.common.topupbills.view.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import kotlinx.android.synthetic.main.view_digital_empty_product_list.view.*

/**
 * @author by resakemal on 22/08/19
 */

class TopupBillsEmptyViewHolder(itemView: View) : AbstractViewHolder<EmptyModel>(itemView) {

    override fun bind(element: EmptyModel) {
        with(itemView) {
            title_empty_product.text = element.title
            desc_empty_product.text = element.description
        }
    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.view_digital_empty_product_list
    }
}