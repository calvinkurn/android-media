package com.tokopedia.shop.score.penalty.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.shop.score.penalty.presentation.model.BasePenaltyPage
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemShopPerformanceErrorUiModel

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

}