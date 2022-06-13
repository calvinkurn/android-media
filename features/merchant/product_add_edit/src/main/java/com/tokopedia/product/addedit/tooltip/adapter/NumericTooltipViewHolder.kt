package com.tokopedia.product.addedit.tooltip.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tooltip.model.NumericTooltipModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by faisalramd on 2020-03-09.
 */

class NumericTooltipViewHolder(val view: View?,
                               private val listener: TooltipTypeFactory.OnItemClickListener?)
    : AbstractViewHolder<NumericTooltipModel>(view) {

    private val tvContent: Typography? = view?.findViewById(R.id.tvContent)
    private val tvNumber: Typography? = view?.findViewById(R.id.tvNumber)

    override fun bind(element: NumericTooltipModel) {
        tvContent?.text = element.title
        if (element.number.isEmpty()) {
            tvNumber?.text = (adapterPosition + 1).toString()
        } else {
            tvNumber?.text = element.number
        }
        itemView.setOnClickListener { listener?.onClick(element, adapterPosition) }
    }

    companion object {
        val LAYOUT = R.layout.item_tooltip_base
    }
}