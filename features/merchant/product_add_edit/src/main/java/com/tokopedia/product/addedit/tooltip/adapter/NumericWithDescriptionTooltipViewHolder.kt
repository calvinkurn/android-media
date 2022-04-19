package com.tokopedia.product.addedit.tooltip.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tooltip.model.NumericWithDescriptionTooltipModel
import com.tokopedia.unifyprinciples.Typography

class NumericWithDescriptionTooltipViewHolder(val view: View?,
                                              private val listener: TooltipTypeFactory.OnItemClickListener?)
    : AbstractViewHolder<NumericWithDescriptionTooltipModel>(view) {

    private val tvContentTitle: Typography? = view?.findViewById(R.id.tvContentTitle)
    private val tvContent: Typography? = view?.findViewById(R.id.tvContent)
    private val tvNumber: Typography? = view?.findViewById(R.id.tvNumber)

    override fun bind(element: NumericWithDescriptionTooltipModel) {
        tvContentTitle?.text = element.title
        tvContent?.text = element.title
        tvNumber?.text = if (element.number.isEmpty()) {
            (adapterPosition + 1).toString()
        } else {
            element.number
        }
        itemView.setOnClickListener { listener?.onClick(element, adapterPosition) }
    }

    companion object {
        val LAYOUT = R.layout.item_tooltip_number_with_description
    }
}