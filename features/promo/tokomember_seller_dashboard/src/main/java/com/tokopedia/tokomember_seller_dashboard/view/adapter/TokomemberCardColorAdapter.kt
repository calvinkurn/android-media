package com.tokopedia.tokomember_seller_dashboard.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberCardColorFactory
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColor
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroHeaderItem

class TokomemberCardColorAdapter(private val visitableList: ArrayList<Visitable<*>>,
                                 private val typeFactory: TokomemberCardColorFactory) :
    BaseAdapter<TokomemberCardColorFactory>(typeFactory, visitableList) {

    fun addItems(data: ArrayList<Visitable<*>>) {
        visitableList.clear()
        visitableList.addAll(data)
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.tag?.let {
            if (it is TokomemberIntroHeaderItem && !it.isVisited) {
                it.isVisited = true
               // typeFactory.listener.onItemDisplayed(it, holder.adapterPosition)
            }
        }
    }

    fun unselectModel(position: Int){
        if( position!=-1){
            visitableList[position]  is TokomemberCardColor
            ( visitableList[position] as TokomemberCardColor).isSelected = false
            notifyItemChanged(position)
        }
    }

    fun selectModel(position: Int){
        if( position!=-1){
            visitableList[position]  is TokomemberCardColor
            ( visitableList[position] as TokomemberCardColor).isSelected = true
            notifyItemChanged(position)
        }
    }
}

interface TokomemberCardColorAdapterListener {
    fun onItemDisplayedCardColor(tokoCardItem: Visitable<*>, position: Int)
    fun onItemClickCardColorSelect(tokoCardItem: Visitable<*>?=null, position: Int)
    fun onItemClickCardColorUnselect(tokoCardItem: Visitable<*>?=null, position: Int)

}
