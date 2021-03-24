package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.toggle
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.model.ItemDetailPenaltyFilterUiModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_shop_score_detail_penalty_filter.view.*

class ItemDetailPenaltyFilterViewHolder(view: View,
                                        private val filterPenaltyListener: FilterPenaltyListener)
    : AbstractViewHolder<ItemDetailPenaltyFilterUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_score_detail_penalty_filter
    }

    override fun bind(element: ItemDetailPenaltyFilterUiModel?) {
        with(itemView) {
            tvPeriodDetailPenalty?.text = getString(R.string.period_date_detail_penalty, element?.periodDetail.orEmpty())
            sortFilterDetailPenalty?.setupSortFilter(element?.itemSortFilterWrapperList)

            ic_detail_penalty_filter?.setOnClickListener {
                filterPenaltyListener.onDateClick()
            }
        }
    }

    private fun SortFilter.setupSortFilter(sortFilterItemList: List<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper>?) {
        sortFilterItems.removeAllViews()

        val itemSortFilterList = ArrayList<SortFilterItem>()

        sortFilterItemList?.map {
            it.sortFilterItem?.let { sortFilterItem -> itemSortFilterList.add(sortFilterItem) }
        }

        addItem(itemSortFilterList)

        itemSortFilterList.forEach {
            it.listener = {
                if (it.type != ChipsUnify.TYPE_DISABLE) {
                    it.toggle()
                    filterPenaltyListener.onChildSortFilterItemClick(it, adapterPosition)
                }
            }
        }

        parentListener = {
            filterPenaltyListener.onParentSortFilterClick()
        }
    }
}