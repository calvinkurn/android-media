package com.tokopedia.addongifting.addonbottomsheet.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class AddOnListAdapter(adapterTypeFactory: AddOnListAdapterTypeFactory) :
        BaseListAdapter<Visitable<*>, AddOnListAdapterTypeFactory>(adapterTypeFactory) {

    fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(AddOnListDiffUtilCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

}