package com.tokopedia.shop.score.penalty.presentation.adapter.filter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.shop.score.common.ShopScoreConstant.TITLE_TYPE_PENALTY
import com.tokopedia.shop.score.penalty.presentation.model.ChipsFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemSortFilterPenaltyUiModel
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
            when (item) {
                is PenaltyFilterUiModel -> {
                    val chipsIndex = visitables.indexOf(item)
                    item.chipsFilterList = (dataList[index] as? PenaltyFilterUiModel)?.chipsFilterList.orEmpty()
                    if (chipsIndex != RecyclerView.NO_POSITION) {
                        notifyItemChanged(chipsIndex, PAYLOAD_CHIPS_FILTER)
                    }
                }
                is PenaltyFilterDateUiModel -> {
                    val dateIndex = visitables.indexOf(item)
                    (dataList[index] as? PenaltyFilterDateUiModel)?.let {
                        item.startDate = it.startDate
                        item.endDate = it.endDate
                        item.defaultStartDate = it.defaultStartDate
                        item.defaultEndDate = it.defaultEndDate
                        item.completeDate = it.completeDate
                    }
                    if (dateIndex != RecyclerView.NO_POSITION) {
                        notifyItemChanged(dateIndex)
                    }
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

    fun updateFilterSelected(
        wrapperList: List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>
    ) {
        val updateIndex =
            visitables.filterIsInstance<PenaltyFilterUiModel>().find { it.title == TITLE_TYPE_PENALTY }
        val chipsIndex = visitables.indexOf(updateIndex)
        visitables.filterIsInstance<PenaltyFilterUiModel>()
            .find { it.title == TITLE_TYPE_PENALTY }?.run {
                chipsFilterList = chipsFilterList.map {
                    ChipsFilterPenaltyUiModel(
                        title = it.title,
                        isSelected = wrapperList.find { sortFilter ->
                            sortFilter.idFilter == it.value
                        }?.isSelected ?: it.isSelected,
                        value = it.value
                    )
                }
                shownFilterList = wrapperList.map {
                    ChipsFilterPenaltyUiModel(
                        title = it.title,
                        isSelected = it.isSelected,
                        value = it.idFilter
                    )
                }
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
                    dateSelected.first,
                    dateSelected.second,
                    maxDateSelected.first,
                    maxDateSelected.second,
                    visitable.initialStartDate,
                    visitable.initialEndDate,
                    completeDate
                )
                notifyItemChanged(index)
            }
        }
    }

}
