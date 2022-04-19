package com.tokopedia.shop.score.penalty.presentation.adapter.filter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel

class FilterPenaltyAdapter(adapterFactory: FilterPenaltyAdapterFactory) :
    BaseAdapter<FilterPenaltyAdapterFactory>(adapterFactory) {

    companion object {
        const val PAYLOAD_CHIPS_FILTER = 108
    }

    fun updateData(dataList: List<BaseFilterPenaltyPage>) {
        visitables.clear()
        visitables.addAll(dataList)
        notifyDataSetChanged()
    }

    fun resetFilterSelected(dataList: List<PenaltyFilterUiModel>) {
        visitables.filterIsInstance<PenaltyFilterUiModel>().mapIndexed { index, item ->
            val chipsIndex = visitables.indexOf(item)
            item.chipsFilterList = dataList[index].chipsFilterList
            if (chipsIndex != RecyclerView.NO_POSITION) {
                notifyItemChanged(chipsIndex, PAYLOAD_CHIPS_FILTER)
            }
        }
    }

    fun updateFilterSelected(
        chipFilterList: List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>,
        title: String
    ) {
        val updateIndex =
            visitables.filterIsInstance<PenaltyFilterUiModel>().find { it.title == title }
        val chipsIndex = visitables.indexOf(updateIndex)
        visitables.filterIsInstance<PenaltyFilterUiModel>()
            .find { it.title == title }?.chipsFilterList = chipFilterList
        if (chipsIndex != RecyclerView.NO_POSITION) {
            notifyItemChanged(chipsIndex, PAYLOAD_CHIPS_FILTER)
        }
    }
}