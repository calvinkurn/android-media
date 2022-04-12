package com.tokopedia.tokomember_seller_dashboard.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberIntroFactory
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroHeaderItem

class TokomemberIntroAdapter(private val visitableList: ArrayList<Visitable<*>>,
                  private val typeFactory: TokomemberIntroFactory) :
    BaseAdapter<TokomemberIntroFactory>(typeFactory, visitableList) {

    fun addItems(data: ArrayList<Visitable<*>>) {
        visitableList.clear()
        visitableList.addAll(data)
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.tag?.let {
            if (it is TokomemberIntroHeaderItem && !it.isVisited) {
                it.isVisited = true
                typeFactory.listener.onItemDisplayed(it, holder.adapterPosition)
            }
        }
    }

}

interface TokomemberIntroAdapterListener {
    fun onItemDisplayed(tokoIntroItem: Visitable<*>, position: Int)
}