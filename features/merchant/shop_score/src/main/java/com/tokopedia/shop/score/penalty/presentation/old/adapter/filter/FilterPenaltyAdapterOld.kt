package com.tokopedia.shop.score.penalty.presentation.old.adapter.filter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyAdapterFactory
import com.tokopedia.shop.score.penalty.presentation.model.ChipsFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.old.model.PenaltyFilterUiModelOld

class FilterPenaltyAdapterOld(adapterFactory: FilterPenaltyAdapterFactory) :
    BaseAdapter<FilterPenaltyAdapterFactory>(adapterFactory) {

    companion object {
        const val PAYLOAD_CHIPS_FILTER = 108
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(dataList: List<BaseFilterPenaltyPage>) {
        visitables.clear()
        visitables.addAll(dataList)
        notifyDataSetChanged()
    }

    fun resetFilterSelected(dataList: List<PenaltyFilterUiModelOld>) {
        visitables.filterIsInstance<PenaltyFilterUiModelOld>().mapIndexed { index, item ->
            val chipsIndex = visitables.indexOf(item)
            item.chipsFilterList = dataList[index].chipsFilterList
            if (chipsIndex != RecyclerView.NO_POSITION) {
                notifyItemChanged(chipsIndex, PAYLOAD_CHIPS_FILTER)
            }
        }
    }

    fun updateFilterSelected(
        chipFilterList: List<ChipsFilterPenaltyUiModel>,
        title: String
    ) {
        val updateIndex =
            visitables.filterIsInstance<PenaltyFilterUiModelOld>().find { it.title == title }
        val chipsIndex = visitables.indexOf(updateIndex)
        visitables.filterIsInstance<PenaltyFilterUiModelOld>()
            .find { it.title == title }?.chipsFilterList = chipFilterList
        if (chipsIndex != RecyclerView.NO_POSITION) {
            notifyItemChanged(chipsIndex, PAYLOAD_CHIPS_FILTER)
        }
    }
}
