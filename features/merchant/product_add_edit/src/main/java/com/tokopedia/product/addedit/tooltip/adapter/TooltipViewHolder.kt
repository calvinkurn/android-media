package com.tokopedia.product.addedit.tooltip.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tooltip.model.TooltipModel
import kotlinx.android.synthetic.main.item_product_choosing_tips.view.*

/**
 * Created by faisalramd on 2020-03-09.
 */

class TooltipViewHolder(val view: View?,
                        private val listener: TooltipTypeFactory.OnItemClickListener?)
    : AbstractViewHolder<TooltipModel>(view) {
    override fun bind(element: TooltipModel) {
        itemView.textViewTitle.text = element.title
        itemView.setOnClickListener { listener?.onClick(element, adapterPosition) }
    }

    companion object {
        val LAYOUT = R.layout.item_product_choosing_tips
    }
}