package com.tokopedia.product.addedit.tooltip.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tooltip.model.TooltipModel
import kotlinx.android.synthetic.main.item_product_choosing_tips.view.*

class TooltipTypeFactory: BaseAdapterTypeFactory(){
    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    fun type(formInput: TooltipModel): Int = ChoosingTipsViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            ChoosingTipsViewHolder.LAYOUT -> ChoosingTipsViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

    class ChoosingTipsViewHolder(val view: View?, private val listener: OnItemClickListener?): AbstractViewHolder<TooltipModel>(view) {
        override fun bind(element: TooltipModel) {
            itemView.textViewTitle.text = element.title
            itemView.textViewSubtitle.text = element.subtitle
            ImageHandler.LoadImage(itemView.imageViewCatalog, element.image)
            itemView.setOnClickListener { listener?.onClick(element, adapterPosition) }
        }

        companion object {
            val LAYOUT = R.layout.item_product_choosing_tips
        }
    }

    interface OnItemClickListener {
        fun onClick(tooltipModel: TooltipModel, position: Int)
    }
}