package com.tokopedia.deals.common.ui.adapter.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.DealChipsListActionListener
import com.tokopedia.deals.common.ui.dataview.DealsChipsDataView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_deals_chips_list.view.*

class DealsChipsViewHolder(
    itemView: View,
    private val chipsListener: DealChipsListActionListener
) : BaseViewHolder(itemView) {

    private var chips: DealsChipsDataView = DealsChipsDataView()

    fun bind(chips: DealsChipsDataView) {
        itemView.run {
            if (!chips.isLoaded) {
                shimmering.show()
            } else {
                shimmering.gone()
                sort_filter_deals_category.visible()
                this@DealsChipsViewHolder.chips = chips
                showChips()
            }
        }
    }

    private fun showChips() {
        val filterItems = arrayListOf<SortFilterItem>()
        try {
            val showingChips = if (chips.chipList.size > chips.showingLimit) {
                chips.chipList.subList(0, chips.showingLimit - 1)
            } else {
                chips.chipList
            }
            showingChips.forEach {
                val item = SortFilterItem(it.title)
                filterItems.add(item)
            }
        } catch (e: Exception) { }

        itemView.sort_filter_deals_category.run {
            filterItems.forEachIndexed { index, sortFilterItem ->

                if (chips.chipList[index].isSelected) {
                    sortFilterItem.type = ChipsUnify.TYPE_SELECTED
                }

                sortFilterItem.listener = {
                    sortFilterItem.toggle()

                    val mutableChipList = chips.chipList.toMutableList()
                    val chipItem = mutableChipList[index]

                    val isItemSelected = filterItems[index].type == ChipsUnify.TYPE_SELECTED
                    mutableChipList[index] = chipItem.copy(isSelected = isItemSelected)
                    chips = chips.copy(chipList = mutableChipList)

                    chipsListener.onChipClicked(chips.copy(chipList = mutableChipList))
                }
            }

            addItem(filterItems)

            if (chips.chipList.size <= chips.showingLimit) {
                sortFilterPrefix.hide()
            } else {
                sortFilterPrefix.setOnClickListener {
                    chipsListener.onFilterChipClicked(chips)
                }
            }
        }

        checkIfAdditionalFilterSelected()
    }

    private var additionalSelectedFilterCount = 0

    fun updateChips(chips: DealsChipsDataView) {
        itemView.sort_filter_deals_category.let {
            it.indicatorCounter -= additionalSelectedFilterCount

            this.chips = chips
            additionalSelectedFilterCount = 0
            if (chips.chipList.size > it.chipItems.size) {
                for (i in it.chipItems.size until chips.chipList.size) {
                    if (chips.chipList[i].isSelected) additionalSelectedFilterCount++
                }
            }

            it.chipItems.forEachIndexed { index, sortFilterItem ->
                sortFilterItem.type = if (chips.chipList[index].isSelected) ChipsUnify.TYPE_SELECTED
                else ChipsUnify.TYPE_NORMAL
            }

            it.indicatorCounter += additionalSelectedFilterCount
        }
    }

    private fun checkIfAdditionalFilterSelected() {
        itemView.sort_filter_deals_category.let {
            additionalSelectedFilterCount = 0
            if (chips.chipList.size > it.chipItems.size) {
                for (i in it.chipItems.size until chips.chipList.size) {
                    if (chips.chipList[i].isSelected) additionalSelectedFilterCount++
                }
            }
            it.indicatorCounter += additionalSelectedFilterCount
        }
    }

    private fun SortFilterItem.toggle() {
        type = if (type == ChipsUnify.TYPE_NORMAL) {
            itemView.sort_filter_deals_category.indicatorCounter++
            ChipsUnify.TYPE_SELECTED
        } else {
            itemView.sort_filter_deals_category.indicatorCounter--
            ChipsUnify.TYPE_NORMAL
        }
    }

    companion object {
        val LAYOUT = R.layout.item_deals_chips_list
    }
}