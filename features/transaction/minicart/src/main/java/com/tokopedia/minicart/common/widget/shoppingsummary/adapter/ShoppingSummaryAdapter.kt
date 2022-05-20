package com.tokopedia.minicart.common.widget.shoppingsummary.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class ShoppingSummaryAdapter(adapterTypeFactory: ShoppingSummaryAdapterTypeFactory) :
    BaseListAdapter<Visitable<*>, ShoppingSummaryAdapterTypeFactory>(adapterTypeFactory) {

    fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(ShoppingSummaryDiffUtilCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }
}