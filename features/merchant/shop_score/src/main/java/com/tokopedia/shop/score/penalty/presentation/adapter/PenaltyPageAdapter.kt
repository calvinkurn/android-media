package com.tokopedia.shop.score.penalty.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.score.penalty.presentation.model.BasePenaltyPage
import com.tokopedia.shop.score.penalty.presentation.model.ItemDetailPenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.unifycomponents.ChipsUnify

class PenaltyPageAdapter(penaltyPageAdapterFactory: PenaltyPageAdapterFactory):
        BaseListAdapter<Visitable<*>, PenaltyPageAdapterFactory>(penaltyPageAdapterFactory) {

    companion object {
        const val PAYLOAD_PENALTY_FILTER = 102
    }

    fun setPenaltyListDetailData(penaltyListUiModel: List<ItemPenaltyUiModel>) {
        val lastIndex = visitables.size
        visitables.addAll(penaltyListUiModel)
        notifyItemRangeInserted(lastIndex, penaltyListUiModel.size)
    }

    fun setPenaltyData(penaltyListUiModel: List<BasePenaltyPage>) {
        visitables.clear()
        visitables.addAll(penaltyListUiModel)
        notifyDataSetChanged()
    }

    fun updateSortFilterPenaltyFromBottomSheet(chipsPenaltyList: List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>?) {
//        val sortFilterIndex = visitables.indexOfFirst { it is ItemDetailPenaltyFilterUiModel }
//        visitables.filterIsInstance<ItemDetailPenaltyFilterUiModel>().firstOrNull()?.itemSortFilterWrapperList?.mapIndexed { index, item ->
//            item.isSelected = chipsPenaltyList?.getOrNull(index)?.isSelected ?: false
//        }
//        if (sortFilterIndex != -1) {
//            notifyItemChanged(sortFilterIndex)
//        }

        //need adjust lazy load
        //notifyItemRangeRemoved()
    }
}