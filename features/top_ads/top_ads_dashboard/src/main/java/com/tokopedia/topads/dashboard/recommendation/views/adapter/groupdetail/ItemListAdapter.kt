package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail

import android.annotation.SuppressLint
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ItemListUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.ItemListTypeFactory

class ItemListAdapter(factory: ItemListTypeFactory?) :
    BaseListAdapter<Visitable<*>, ItemListTypeFactory>(factory) {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ItemListUiModel>) {
        visitables?.clear()
        visitables?.addAll(data)
        notifyDataSetChanged()
    }

}
