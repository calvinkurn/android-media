package com.tokopedia.shop.score.penalty.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.shop.score.penalty.presentation.model.BasePenaltyPage
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel

class PenaltyPageAdapter(penaltyPageAdapterFactory: PenaltyPageAdapterFactory):
        BaseListAdapter<Visitable<*>, PenaltyPageAdapterFactory>(penaltyPageAdapterFactory) {

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

    fun hidePenaltyData() {
        val itemPenaltySize = visitables.count { it is ItemPenaltyUiModel }
        visitables.removeAll { it is ItemPenaltyUiModel }
        notifyItemRangeRemoved(visitables.size, itemPenaltySize)
    }

    fun setPenaltyLoading() {
        if (visitables.getOrNull(lastIndex) !is LoadingMoreModel){
            visitables.add(loadingMoreModel)
            notifyItemInserted(lastIndex)
        }
    }

    fun setEmptyStatePenalty() {
        if (visitables.getOrNull(lastIndex) !is EmptyModel) {
            visitables.add(EmptyModel())
            notifyItemInserted(lastIndex)
        }
    }

    fun removePenaltyNotFound() {
        if (visitables.getOrNull(lastIndex) is EmptyModel) {
            visitables.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }
}