package com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import com.tokopedia.unifycomponents.ChipsUnify

class RepurchaseSortFilterViewHolder(
    itemView: View,
    private val listener: SortFilterListener
) : AbstractViewHolder<RepurchaseSortFilterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_repurchase_sort_filter
    }

    private val filterItems: ArrayList<SortFilterItem> = arrayListOf()

    private val sortFilter: SortFilter? by lazy { itemView.findViewById(R.id.sort_filter) }

    override fun bind(data: RepurchaseSortFilterUiModel) {
        clearSortFilterItems()
        addSortFilterItems(data)
        setupChipRightIcon()
        setupClearButton(data)
    }

    private fun addSortFilterItems(data: RepurchaseSortFilterUiModel) {
        data.sortFilterList.forEach {
            val selectedItems = it.selectedItem?.title.orEmpty()

            val title = if(selectedItems.isNotEmpty() && it.qtyFormat != null) {
                val selectedFilterCount = selectedItems.count().orZero()
                itemView.context.getString(it.qtyFormat, selectedFilterCount)
            } else {
                getString(it.title)
            }

            val item = SortFilterItem(title)

            filterItems.add(item)
            sortFilter?.addItem(filterItems)

            item.apply {
                type = if(selectedItems.isNotEmpty()) {
                    ChipsUnify.TYPE_SELECTED
                } else {
                    it.chipType
                }
                listener = {
                    onClickSortFilterItem(it)
                }
                refChipUnify.setChevronClickListener {
                    onClickSortFilterItem(it)
                }
            }
        }
    }

    private fun setupClearButton(data: RepurchaseSortFilterUiModel) {
        sortFilter?.sortFilterPrefix?.setOnClickListener {
            clearAllFilters(data)
        }
    }

    private fun setupChipRightIcon() {
        filterItems.forEach { item ->
            item.refChipUnify.chip_right_icon.show()
        }
    }

    private fun onClickSortFilterItem(filter: RepurchaseSortFilter) {
        when (filter.type) {
            RepurchaseSortFilterType.SORT -> listener.onClickSortFilter()
            RepurchaseSortFilterType.DATE_FILTER -> listener.onClickDateFilter()
            RepurchaseSortFilterType.CATEGORY_FILTER -> listener.onClickCategoryFilter()
        }
    }

    private fun clearAllFilters(data: RepurchaseSortFilterUiModel) {
        sortFilter?.resetAllFilters()
        filterItems.forEachIndexed { index, item ->
            val sortFilter = data.sortFilterList[index]
            val title = getString(sortFilter.title)
            item.title = title
        }
        listener.onClearAllFilter()
    }

    private fun clearSortFilterItems() {
        filterItems.clear()
    }

    interface SortFilterListener {
        fun onClickSortFilter()
        fun onClickDateFilter()
        fun onClickCategoryFilter()
        fun onClearAllFilter()
    }
}