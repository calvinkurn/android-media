package com.tokopedia.purchase_platform.features.promo.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class PromoCheckoutAdapter(adapterTypeFactory: PromoCheckoutMarketplaceAdapterTypeFactory) :
        BaseListAdapter<Visitable<*>, PromoCheckoutMarketplaceAdapterTypeFactory>(adapterTypeFactory) {

    fun addVisitable(visitable: Visitable<*>) {
        val newList: MutableList<Visitable<*>> = mutableListOf()
        newList.addAll(list)
        newList.add(visitable)
        updateList(newList)
    }

    fun addVisitableList(visitableList: List<Visitable<*>>) {
        val newList: MutableList<Visitable<*>> = mutableListOf()
        newList.addAll(list)
        newList.addAll(visitableList)
        updateList(newList)
    }

    fun addVisitableList(indexStart: Int, visitableList: List<Visitable<*>>) {
        val newList: MutableList<Visitable<*>> = mutableListOf()
        newList.addAll(list)
        var index = indexStart
        visitableList.forEach {
            newList.add(index++, it)
        }
        updateList(newList)
    }

    fun modifyData(itemPosition: Int) {
        notifyItemChanged(itemPosition)
    }

    fun removeList(visitableList: List<Visitable<*>>) {
        val newList: MutableList<Visitable<*>> = mutableListOf()
        newList.addAll(list)
        newList.removeAll(visitableList)
        updateList(newList)
    }

    fun modifyDataList(visitableListMap: Map<Int, Visitable<*>>) {
        visitableListMap.forEach {
            notifyItemChanged(it.key)
        }
    }

    private fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(PromoDiffUtilCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

}