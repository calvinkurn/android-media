package com.tokopedia.tokomember_seller_dashboard.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberCardColorFactory
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
}

interface TokomemberCardColorAdapterListener {
    fun onItemDisplayedCardColor(tokoIntroItem: Visitable<*>, position: Int)
}
