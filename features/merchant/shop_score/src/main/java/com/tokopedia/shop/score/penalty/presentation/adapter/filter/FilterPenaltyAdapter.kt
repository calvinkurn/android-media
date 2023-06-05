package com.tokopedia.shop.score.penalty.presentation.adapter.filter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.shop.score.penalty.presentation.model.ChipsFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterDateUiModel
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

    fun resetFilterSelected(dataList: List<BaseFilterPenaltyPage>) {
        visitables.mapIndexed { index, item ->
            if (item is PenaltyFilterUiModel) {
                val chipsIndex = visitables.indexOf(item)
                item.chipsFilterList = (dataList[index] as? PenaltyFilterUiModel)?.chipsFilterList.orEmpty()
                if (chipsIndex != RecyclerView.NO_POSITION) {
                    notifyItemChanged(chipsIndex, PAYLOAD_CHIPS_FILTER)
                }
            }
        }
    }

    fun updateFilterSelected(
        chipFilterList: List<ChipsFilterPenaltyUiModel>,
        title: String
    ) {
        val updateIndex =
            visitables.filterIsInstance<PenaltyFilterUiModel>().find { it.title == title }
        val chipsIndex = visitables.indexOf(updateIndex)
        visitables.filterIsInstance<PenaltyFilterUiModel>()
            .find { it.title == title }?.run {
                chipsFilterList = chipFilterList
            }
        if (chipsIndex != RecyclerView.NO_POSITION) {
            notifyItemChanged(chipsIndex, PAYLOAD_CHIPS_FILTER)
        }
    }

    fun updateDateSelected(
        dateSelected: Pair<String, String>,
        maxDateSelected: Pair<String, String>,
        completeDate: String
    ) {
        visitables.forEachIndexed { index, visitable ->
            if (visitable is PenaltyFilterDateUiModel) {
                visitables[index] = PenaltyFilterDateUiModel(
                    maxDateSelected.first,
                    maxDateSelected.second,
                    dateSelected.first,
                    dateSelected.second,
                    completeDate
                )
                notifyItemChanged(index)
            }
        }
    }

}
