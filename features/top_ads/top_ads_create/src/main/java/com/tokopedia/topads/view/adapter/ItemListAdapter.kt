package com.tokopedia.topads.view.adapter

import android.annotation.SuppressLint
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.topads.view.uimodel.ItemListUiModel

class ItemListAdapter(private val factory: ListBottomSheetItemFactory) :
    BaseListAdapter<Visitable<*>, ListBottomSheetItemFactory>(factory) {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ItemListUiModel>) {
        visitables?.clear()
        visitables?.addAll(data)
        notifyDataSetChanged()
    }

}
