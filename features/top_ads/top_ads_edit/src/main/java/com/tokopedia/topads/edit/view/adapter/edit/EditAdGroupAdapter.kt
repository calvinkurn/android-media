package com.tokopedia.topads.edit.view.adapter.edit

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class EditAdGroupAdapter (typeFactory: EditAdGroupTypeFactory?
) : BaseListAdapter<Visitable<*>, EditAdGroupTypeFactory>(typeFactory) {

    fun updateList(newList: List<Visitable<*>>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}