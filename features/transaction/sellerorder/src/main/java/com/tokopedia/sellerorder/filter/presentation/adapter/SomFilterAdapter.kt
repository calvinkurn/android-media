package com.tokopedia.sellerorder.filter.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.sellerorder.filter.presentation.model.BaseSomFilter
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterDateUiModel

class SomFilterAdapter(adapterTypeFactory: SomFilterAdapterTypeFactory): BaseAdapter<SomFilterAdapterTypeFactory>(adapterTypeFactory) {

    companion object {
        const val PAYLOAD_DATE_FILTER = 104
    }

    fun updateData(dataList: List<BaseSomFilter>) {
        visitables.clear()
        visitables.addAll(dataList)
        notifyDataSetChanged()
    }

    fun setEmptyState(emptyData: EmptyModel) {
        visitables.clear()
        visitables.add(emptyData)
        notifyDataSetChanged()
    }

    fun updateDateFilterText(startDate: String, endDate: String) {
        val date = if(startDate.isBlank() && endDate.isBlank()) {
            ""
        } else if(endDate.isBlank()) {
            startDate
        } else {
            "$startDate - $endDate"
        }
        val dateIndex = visitables.indexOfFirst { it is SomFilterDateUiModel }
        visitables.find { it is SomFilterDateUiModel }?.also {
            (it as SomFilterDateUiModel).date = date
        }
        if (dateIndex != -1) {
            notifyItemChanged(dateIndex, PAYLOAD_DATE_FILTER)
        }
    }
}