package com.tokopedia.minicart.cartlist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class MiniCartListAdapter(adapterTypeFactory: MiniCartListAdapterTypeFactory) :
        BaseListAdapter<Visitable<*>, MiniCartListAdapterTypeFactory>(adapterTypeFactory) {

    fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(MiniCartListDiffUtilCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

}