package com.tokopedia.shop.score.penalty.presentation.adapter.filter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter

class FilterPenaltyTypesAdapter(adapterFactory: FilterPenaltyTypesAdapterFactory) :
    BaseAdapter<FilterPenaltyTypesAdapterFactory>(adapterFactory) {

    fun updateData(dataList: List<Visitable<*>>) {
        visitables.clear()
        visitables.addAll(dataList)
        notifyDataSetChanged()
    }

}
