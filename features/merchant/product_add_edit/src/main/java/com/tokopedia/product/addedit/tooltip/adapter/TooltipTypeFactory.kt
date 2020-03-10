package com.tokopedia.product.addedit.tooltip.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.addedit.tooltip.model.ImageTooltipModel
import com.tokopedia.product.addedit.tooltip.model.NumericTooltipModel
import com.tokopedia.product.addedit.tooltip.model.TooltipModel

class TooltipTypeFactory: BaseAdapterTypeFactory(){
    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    fun type(tooltipModel: TooltipModel): Int = TooltipViewHolder.LAYOUT

    fun type(tooltipModel: ImageTooltipModel): Int = TooltipViewHolder.LAYOUT

    fun type(tooltipModel: NumericTooltipModel): Int = TooltipViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            TooltipViewHolder.LAYOUT -> TooltipViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

    interface OnItemClickListener {
        fun onClick(tooltipModel: TooltipModel, position: Int)
    }
}