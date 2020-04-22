package com.tokopedia.product.addedit.optionpicker.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.addedit.optionpicker.model.OptionModel

class OptionTypeFactory: BaseAdapterTypeFactory(){
    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    fun type(type: OptionModel): Int = OptionViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            OptionViewHolder.LAYOUT -> OptionViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

    interface OnItemClickListener {
        fun onClick(selectedText: String, selectedPosition: Int)
    }
}