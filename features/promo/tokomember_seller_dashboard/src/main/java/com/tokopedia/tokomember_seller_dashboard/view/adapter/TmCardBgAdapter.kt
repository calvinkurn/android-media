package com.tokopedia.tokomember_seller_dashboard.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TmCardBgFactory

class TmCardBgAdapter(private val visitableList: ArrayList<Visitable<*>>,
                              private val typeFactory: TmCardBgFactory) :
    BaseAdapter<TmCardBgFactory>(typeFactory, visitableList) {

    fun addItems(data: ArrayList<Visitable<*>>) {
        visitableList.clear()
        visitableList.addAll(data)
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
    }

}

interface TokomemberCardBgAdapterListener {
    fun onItemDisplayedCardBg(tokoCardItem: Visitable<*>?=null, position: Int)
    fun onItemClickCardCBg(tokoCardItem: Visitable<*>?=null, position: Int)
}
