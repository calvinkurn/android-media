package com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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

        private const val FIRST_ITEM_INDEX = 0
    }

    private val filterItems: ArrayList<SortFilterItem> = arrayListOf()

    private val sortFilter: SortFilter? by lazy { itemView.findViewById(R.id.sort_filter) }

    override fun bind(data: RepurchaseSortFilterUiModel) {
        clearSortFilterItems()
        addSortFilterItems(data)
        setupClearButton(data)
        setupLayout(data)
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
            setupLayout(data)
        }
    }

    private fun setupLayout(data: RepurchaseSortFilterUiModel) {
        val filterApplied = data.sortFilterList.any {
            !it.selectedItem?.id.isNullOrEmpty()
        }
        setupSortFilterMargin(filterApplied)
        setupFilterChipMargin(filterApplied)
    }

    private fun setupSortFilterMargin(filterApplied: Boolean) {
        val resources = itemView.context.resources
        val marginStart = if (filterApplied) {
            resources.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4
            )
        } else {
            resources.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.layout_lvl0
            )
        }
        val gridLayoutParams = sortFilter?.layoutParams
            as? StaggeredGridLayoutManager.LayoutParams
        gridLayoutParams?.marginStart = marginStart
        sortFilter?.layoutParams = gridLayoutParams
    }

    private fun setupFilterChipMargin(filterApplied: Boolean) {
        val resources = itemView.context.resources
        val marginEnd = resources.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2
        )
        val marginStart = resources.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4
        )
        filterItems.forEachIndexed { index, item ->
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            item.refChipUnify.apply {
                if (index == FIRST_ITEM_INDEX && !filterApplied) {
                    lp.marginStart = marginStart
                    lp.marginEnd = marginEnd
                    layoutParams = lp
                }
                if (index == FIRST_ITEM_INDEX && filterApplied) {
                    lp.marginEnd = marginEnd
                    layoutParams = lp
                }
                chip_right_icon.show()
            }
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