package com.tokopedia.addongifting.addonbottomsheet.view.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class AddOnListAdapter(adapterTypeFactory: AddOnListAdapterTypeFactory) :
        BaseListAdapter<Visitable<*>, AddOnListAdapterTypeFactory>(adapterTypeFactory) {

    fun modifyData(itemPosition: Int) {
        if (itemPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(itemPosition)
        }
    }

    fun addVisitable(visitable: Visitable<*>) {
        val newList: MutableList<Visitable<*>> = mutableListOf()
        newList.addAll(list)
        newList.add(visitable)
        updateList(newList)
    }

    private fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(AddOnListDiffUtilCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

}