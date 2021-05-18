package com.tokopedia.minicart.ui.cartlist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class MiniCartListAdapter(adapterTypeFactory: MiniCartListAdapterTypeFactory) :
        BaseListAdapter<Visitable<*>, MiniCartListAdapterTypeFactory>(adapterTypeFactory) {

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

    fun modifyDataList(indexList: List<Int>) {
        indexList.forEach {
            modifyData(it)
        }
    }

    fun removeData(visitable: Visitable<*>) {
        val newList: MutableList<Visitable<*>> = mutableListOf()
        newList.addAll(list)
        newList.remove(visitable)
        updateList(newList)
    }

    fun removeDataList(visitableList: List<Visitable<*>>) {
        val newList: MutableList<Visitable<*>> = mutableListOf()
        newList.addAll(list)
        newList.removeAll(visitableList)
        updateList(newList)
    }

    private fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(MiniCartListDiffUtilCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

}