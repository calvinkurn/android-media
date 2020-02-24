package com.tokopedia.purchase_platform.features.promo.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class PromoCheckoutAdapter(adapterTypeFactory: PromoCheckoutMarketplaceAdapterTypeFactory) :
        BaseListAdapter<Visitable<*>, PromoCheckoutAdapterTypeFactory>(adapterTypeFactory) {

    fun addVisitable(visitable: Visitable<*>) {
        val newList: MutableList<Visitable<*>> = mutableListOf()
        newList.addAll(list)
        newList.add(visitable)
        updateList(newList)
    }

    fun addVisitables(visitableList: ArrayList<Visitable<*>>) {
        val newList: MutableList<Visitable<*>> = mutableListOf()
        newList.addAll(list)
        newList.addAll(visitableList)
        updateList(newList)
    }

    private fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(PromoDiffUtilCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

}