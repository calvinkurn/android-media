package com.tokopedia.tokomember_seller_dashboard.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberCardBgFactory

class TokomemberCardBgAdapter(private val visitableList: ArrayList<Visitable<*>>,
                              private val typeFactory: TokomemberCardBgFactory) :
    BaseAdapter<TokomemberCardBgFactory>(typeFactory, visitableList) {

    fun addItems(data: ArrayList<Visitable<*>>) {
        visitableList.clear()
        visitableList.addAll(data)
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
    }
}

interface TokomemberCardBgAdapterListener {
    fun onItemDisplayedCardBg(tokoCardItem: Visitable<*>, position: Int)
    fun onItemClickCardCBg(tokoCardItem: Visitable<*>, position: Int)
}
