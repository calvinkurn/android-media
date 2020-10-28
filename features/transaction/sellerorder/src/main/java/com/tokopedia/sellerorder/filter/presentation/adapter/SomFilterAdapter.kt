package com.tokopedia.sellerorder.filter.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_DATE
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel

class SomFilterAdapter(adapterTypeFactory: SomFilterAdapterTypeFactory): BaseAdapter<SomFilterAdapterTypeFactory>(adapterTypeFactory) {

    companion object {
        const val PAYLOAD_DATE_FILTER = 104
    }

    fun updateData(dataList: List<SomFilterUiModel>) {
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
        val date = "$startDate - $endDate"
        val topicIndex = visitables.indexOfFirst { it is SomFilterUiModel }
        visitables.filterIsInstance<SomFilterUiModel>().firstOrNull { it.nameFilter == FILTER_DATE }?.date = date
        if (topicIndex != -1) {
            notifyItemChanged(topicIndex, PAYLOAD_DATE_FILTER)
        }
    }
}