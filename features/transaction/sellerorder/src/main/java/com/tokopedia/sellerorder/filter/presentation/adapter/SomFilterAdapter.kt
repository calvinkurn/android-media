package com.tokopedia.sellerorder.filter.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.sellerorder.filter.presentation.model.*

class SomFilterAdapter(adapterTypeFactory: SomFilterAdapterTypeFactory) : BaseAdapter<SomFilterAdapterTypeFactory>(adapterTypeFactory) {

    companion object {
        const val PAYLOAD_DATE_FILTER = 104
        const val PAYLOAD_CHIPS_FILTER = 108
    }

    fun updateData(dataList: List<BaseSomFilter>) {
        val callBack = SomFilterDiffUtilCallback(visitables, dataList)
        val diffResult = DiffUtil.calculateDiff(callBack)
        visitables.clear()
        visitables.addAll(dataList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun resetFilterSelected(dataList: List<SomFilterUiModel>) {
        visitables.filterIsInstance<SomFilterUiModel>().mapIndexed { index, somFilter ->
            val chipsIndex = visitables.indexOf(somFilter)
            somFilter.somFilterData = dataList[index].somFilterData
            if(chipsIndex != -1) {
                notifyItemChanged(chipsIndex, PAYLOAD_CHIPS_FILTER)
            }
        }
        updateDateFilterText()
    }

    fun setEmptyState(emptyData: SomFilterEmptyUiModel) {
        val visitableEmpty = mutableListOf<BaseSomFilter>(emptyData)
        val callBack = SomFilterDiffUtilCallback(visitables, visitableEmpty)
        val diffResult = DiffUtil.calculateDiff(callBack)
        visitables.clear()
        visitables.addAll(visitableEmpty)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateChipsSelected(chipsList: List<SomFilterChipsUiModel>, nameFilter: String) {
        val updateIndex = visitables.filterIsInstance<SomFilterUiModel>().firstOrNull { it.nameFilter == nameFilter }
        val chipsIndex = visitables.indexOf(updateIndex)
        visitables.filterIsInstance<SomFilterUiModel>().firstOrNull { it.nameFilter == nameFilter}?.somFilterData = chipsList
        visitables.filterIsInstance<SomFilterUiModel>().firstOrNull { it.nameFilter == nameFilter}?.nameFilter = nameFilter
        if(chipsIndex != -1) {
            notifyItemChanged(chipsIndex, PAYLOAD_CHIPS_FILTER)
        }
    }

    fun updateDateFilterText(date: String = "") {
        val dateIndex = visitables.indexOfFirst { it is SomFilterDateUiModel }
        visitables.find { it is SomFilterDateUiModel }?.also {
            (it as SomFilterDateUiModel).date = date
        }
        if (dateIndex != -1) {
            notifyItemChanged(dateIndex, PAYLOAD_DATE_FILTER)
        }
    }
}