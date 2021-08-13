package com.tokopedia.product.addedit.tooltip.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tooltip.model.NumericWithDescriptionTooltipModel
import kotlinx.android.synthetic.main.item_tooltip_number_with_description.view.*

class NumericWithDescriptionTooltipViewHolder(val view: View?,
                                              private val listener: TooltipTypeFactory.OnItemClickListener?)
    : AbstractViewHolder<NumericWithDescriptionTooltipModel>(view) {
    override fun bind(element: NumericWithDescriptionTooltipModel) {
        itemView.tvContentTitle.text = element.title
        itemView.tvContent.text = element.title
        if (element.number.isEmpty()) {
            itemView.tvNumber.text = (adapterPosition + 1).toString()
        } else {
            itemView.tvNumber.text = element.number
        }
        itemView.setOnClickListener { listener?.onClick(element, adapterPosition) }
    }

    companion object {
        val LAYOUT = R.layout.item_tooltip_number_with_description
    }
}