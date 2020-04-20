package com.tokopedia.product.addedit.tooltip.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tooltip.model.NumericTooltipModel
import kotlinx.android.synthetic.main.item_tooltip_base.view.*

/**
 * Created by faisalramd on 2020-03-09.
 */

class NumericTooltipViewHolder(val view: View?,
                               private val listener: TooltipTypeFactory.OnItemClickListener?)
    : AbstractViewHolder<NumericTooltipModel>(view) {
    override fun bind(element: NumericTooltipModel) {
        itemView.tvContent.text = element.title
        if (element.number.isEmpty()) {
            itemView.tvNumber.text = (adapterPosition + 1).toString()
        } else {
            itemView.tvNumber.text = element.number
        }
        itemView.setOnClickListener { listener?.onClick(element, adapterPosition) }
    }

    companion object {
        val LAYOUT = R.layout.item_tooltip_base
    }
}