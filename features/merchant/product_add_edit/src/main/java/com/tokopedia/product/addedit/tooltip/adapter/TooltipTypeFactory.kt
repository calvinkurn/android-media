package com.tokopedia.product.addedit.tooltip.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.addedit.tooltip.model.ImageTooltipModel
import com.tokopedia.product.addedit.tooltip.model.NumericTooltipModel
import com.tokopedia.product.addedit.tooltip.model.NumericWithDescriptionTooltipModel
import com.tokopedia.product.addedit.tooltip.model.TooltipModel

class TooltipTypeFactory: BaseAdapterTypeFactory(){
    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    fun type(type: ImageTooltipModel): Int = ImageTooltipViewHolder.LAYOUT

    fun type(type: NumericTooltipModel): Int = NumericTooltipViewHolder.LAYOUT

    fun type(type: NumericWithDescriptionTooltipModel): Int = NumericWithDescriptionTooltipViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            NumericTooltipViewHolder.LAYOUT -> NumericTooltipViewHolder(parent, listener)
            ImageTooltipViewHolder.LAYOUT -> ImageTooltipViewHolder(parent, listener)
            NumericWithDescriptionTooltipViewHolder.LAYOUT -> NumericWithDescriptionTooltipViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

    interface OnItemClickListener {
        fun onClick(tooltipModel: TooltipModel, position: Int)
    }
}