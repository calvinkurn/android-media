package com.tokopedia.shop.score.penalty.presentation.adapter.filter

import android.annotation.SuppressLint
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.shop.score.penalty.presentation.model.filtertypes.ItemPenaltyFilterTypesChecklistUiModel

class FilterPenaltyTypesAdapter(adapterFactory: FilterPenaltyTypesAdapterFactory) :
    BaseAdapter<FilterPenaltyTypesAdapterFactory>(adapterFactory) {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(dataList: List<Visitable<*>>) {
        visitables.clear()
        visitables.addAll(dataList)
        notifyDataSetChanged()
    }

    fun resetData() {
        visitables.mapIndexed { index, visitable ->
            if (visitable is ItemPenaltyFilterTypesChecklistUiModel) {
                visitable.isSelected = false
                notifyItemChanged(index)
            }
        }
    }

    fun getSelectedFilter(): List<Int> {
        return visitables.filterIsInstance<ItemPenaltyFilterTypesChecklistUiModel>()
            .filter { it.isSelected }
            .map { it.filterId }
    }

}
