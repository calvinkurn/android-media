package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemSortFilterPenaltyBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemSortFilterPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.model.ItemSortFilterPenaltyUiModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

class ItemSortFilterPenaltyViewHolder(
    view: View,
    private val itemSortFilterPenaltyListener: ItemSortFilterPenaltyListener
) : AbstractViewHolder<ItemSortFilterPenaltyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_sort_filter_penalty
    }

    private val binding: ItemSortFilterPenaltyBinding? by viewBinding()

    override fun bind(element: ItemSortFilterPenaltyUiModel) {
        binding?.run {
            sortFilterDetailPenalty.run {
                sortFilterItems.removeAllViews()
                indicatorCounter = 0
                setupSortFilter(element.itemSortFilterWrapperList, element.isDateFilterApplied)

                parentListener = {
                    itemSortFilterPenaltyListener.onParentSortFilterClicked()
                }
            }
        }
    }

    private fun SortFilter.setupSortFilter(
        updateSortFilterItemPeriodList: List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>?,
        isDateFilterApplied: Boolean
    ) {
        val sortFilterItemList = ArrayList<SortFilterItem>()

        updateSortFilterItemPeriodList?.map {
            sortFilterItemList.add(
                SortFilterItem(
                    title = it.title,
                    size = ChipsUnify.SIZE_SMALL,
                    type = if (it.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
                )
            )
        }

        post {
            addItem(sortFilterItemList)

            sortFilterItemList.forEach {
                it.listener = {
                    if (it.type != ChipsUnify.TYPE_DISABLE) {
                        itemSortFilterPenaltyListener.onChildSortFilterItemClick(it)
                    }
                }
            }

            indicatorCounter =
                if (isDateFilterApplied) {
                    updateSortFilterItemPeriodList?.count { it.isSelected }.orZero().plus(Int.ONE)
                } else {
                    updateSortFilterItemPeriodList?.count { it.isSelected }.orZero()
                }

            prefixText = if (indicatorCounter > Int.ZERO) {
                getString(R.string.shop_penalty_filter_applied).orEmpty()
            } else {
                String.EMPTY
            }
        }
    }
}
