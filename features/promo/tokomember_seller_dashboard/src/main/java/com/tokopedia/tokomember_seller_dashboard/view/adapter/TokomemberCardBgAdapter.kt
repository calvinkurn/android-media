package com.tokopedia.tokomember_seller_dashboard.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberCardBgFactory
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberCardColorFactory
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroHeaderItem

class TokomemberCardBgAdapter(private val visitableList: ArrayList<Visitable<*>>,
                              private val typeFactory: TokomemberCardBgFactory) :
    BaseAdapter<TokomemberCardBgFactory>(typeFactory, visitableList) {

    fun addItems(data: ArrayList<Visitable<*>>) {
        visitableList.clear()
        visitableList.addAll(data)
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.tag?.let {
            if (it is TokomemberIntroHeaderItem && !it.isVisited) {
                it.isVisited = true
              //  typeFactory.listener.onItemDisplayedCardBg(it, holder.adapterPosition)
            }
        }
    }
}

interface TokomemberCardBgAdapterListener {
    fun onItemDisplayedCardBg(tokoIntroItem: Visitable<*>, position: Int)
}
