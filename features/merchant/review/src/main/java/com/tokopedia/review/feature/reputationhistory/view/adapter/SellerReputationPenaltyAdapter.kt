package com.tokopedia.review.feature.reputationhistory.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.review.feature.reputationhistory.view.model.BaseSellerReputation
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationDateUiModel
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationPenaltyUiModel

class SellerReputationPenaltyAdapter(reputationPenaltyAdapterFactory: ReputationPenaltyAdapterFactory) :
    BaseListAdapter<Visitable<*>, ReputationPenaltyAdapterFactory>(reputationPenaltyAdapterFactory) {

    fun setReputationPenaltyMerge(penaltyListUiModel: List<BaseSellerReputation>) {
        visitables.clear()
        visitables.addAll(penaltyListUiModel)
        notifyDataSetChanged()
    }

    fun removeReputationPenaltyListData() {
        val penaltyListCount = visitables.count { it is ReputationPenaltyUiModel }
        visitables.removeAll { it is ReputationPenaltyUiModel }
        notifyItemRangeRemoved(visitables.size, penaltyListCount)
    }

    fun updateReputationPenaltyListData(penaltyListUiModel: List<BaseSellerReputation>) {
        visitables.addAll(penaltyListUiModel)
        notifyItemRangeInserted(visitables.size, penaltyListUiModel.size)
    }

    fun setEmptyStateReputationPenalty() {
        if (visitables.getOrNull(lastIndex) !is EmptyModel) {
            visitables.add(EmptyModel())
            notifyItemInserted(lastIndex)
        }
    }

    fun removeEmptyStateReputationPenalty() {
        if (visitables.getOrNull(lastIndex) is EmptyModel) {
            visitables.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    fun updateDateFilter(startDate: Long, endDate: Long) {
        val dateIndex = visitables.indexOfFirst { it is ReputationDateUiModel }
        visitables.find { it is ReputationDateUiModel }?.also {
            (it as ReputationDateUiModel).apply {
                this.startDate = startDate
                this.endDate = endDate
            }
        }
        if (dateIndex != -1) {
            notifyItemChanged(dateIndex, PAYLOAD_DATE_FILTER)
        }
    }

    companion object {
        const val PAYLOAD_DATE_FILTER = 785
    }
}