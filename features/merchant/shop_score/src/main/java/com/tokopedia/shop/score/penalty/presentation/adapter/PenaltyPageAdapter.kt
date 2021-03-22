package com.tokopedia.shop.score.penalty.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel

class PenaltyPageAdapter(penaltyPageAdapterFactory: PenaltyPageAdapterFactory):
        BaseListAdapter<Visitable<*>, PenaltyPageAdapterFactory>(penaltyPageAdapterFactory) {

    fun setPenaltyListDetailData(penaltyListUiModel: List<ItemPenaltyUiModel>) {
        val lastIndex = visitables.size
        visitables.addAll(penaltyListUiModel)
        notifyItemRangeInserted(lastIndex, penaltyListUiModel.size)
    }
}