package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.toggle
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemSortFilterPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.model.ItemSortFilterPenaltyUiModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_sort_filter_penalty.view.*

class ItemSortFilterPenaltyViewHolder(view: View, private val itemSortFilterPenaltyListener: ItemSortFilterPenaltyListener): AbstractViewHolder<ItemSortFilterPenaltyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_sort_filter_penalty
    }

    override fun bind(element: ItemSortFilterPenaltyUiModel?) {
        with(itemView) {
            sortFilterDetailPenalty?.apply {
                sortFilterItems.removeAllViews()
                indicatorCounter = 0
                setupSortFilter(element?.itemSortFilterWrapperList)

                parentListener = {
                    itemSortFilterPenaltyListener.onParentSortFilterClicked()
                }
            }
        }
    }

    private fun SortFilter.setupSortFilter(updateSortFilterItemPeriodList: List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>?) {
        val sortFilterItemList = ArrayList<SortFilterItem>()

        updateSortFilterItemPeriodList?.map {
            sortFilterItemList.add(SortFilterItem(
                    title = it.title,
                    size = ChipsUnify.SIZE_SMALL,
                    type = if (it.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
            ))
        }

        addItem(sortFilterItemList)

        sortFilterItemList.forEach {
            it.listener = {
                if (it.type != ChipsUnify.TYPE_DISABLE) {
                    itemSortFilterPenaltyListener.onChildSortFilterItemClick(it)
                    it.toggle()
                }
            }
        }
    }
}