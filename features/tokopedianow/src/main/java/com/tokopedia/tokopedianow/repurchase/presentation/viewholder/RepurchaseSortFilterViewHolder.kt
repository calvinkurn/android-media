package com.tokopedia.tokopedianow.repurchase.presentation.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.DateUtil
import com.tokopedia.tokopedianow.common.util.DateUtil.calendarToStringFormat
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRepurchaseSortFilterBinding
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

class RepurchaseSortFilterViewHolder(
    itemView: View,
    private val listener: SortFilterListener
) : AbstractViewHolder<RepurchaseSortFilterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_repurchase_sort_filter

        private const val FIRST_ITEM_INDEX = 0
        private const val DEFAULT_SORT_FILTER = 2
        private const val DATE_FORMAT = "d/M/yyyy"
    }

    private var binding: ItemTokopedianowRepurchaseSortFilterBinding? by viewBinding()

    private val filterItems: ArrayList<SortFilterItem> = arrayListOf()

    override fun bind(data: RepurchaseSortFilterUiModel) {
        clearSortFilterItems()
        addSortFilterItems(data)
        setupClearButton(data)
        setupLayout(data)
    }

    private fun addSortFilterItems(data: RepurchaseSortFilterUiModel) {
        data.sortFilterList.forEach {
            val selectedItems = it.selectedItem?.title.orEmpty()
            val selectedDateFilter = it.selectedDateFilter

            val title = if(selectedItems.isNotEmpty() && it.titleFormat != null) {
                val selectedFilterCount = selectedItems.count().orZero()
                itemView.context.getString(it.titleFormat, selectedFilterCount)
            } else if (selectedDateFilter != null && it.titleFormat != null) {
                val startDate = DateUtil.getGregorianCalendar(selectedDateFilter.startDate)
                val endDate = DateUtil.getGregorianCalendar(selectedDateFilter.endDate)
                val startDateFormatted = calendarToStringFormat(startDate, DATE_FORMAT)
                val endDateFormatted = calendarToStringFormat(endDate, DATE_FORMAT)
                itemView.context.getString(it.titleFormat, startDateFormatted, endDateFormatted)
            } else {
                getString(it.title)
            }

            val item = SortFilterItem(title)

            filterItems.add(item)
            binding?.sortFilter?.addItem(filterItems)

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
        binding?.sortFilter?.sortFilterPrefix?.setOnClickListener {
            clearAllFilters(data)
            setupLayout(data)
        }
    }

    private fun setupLayout(data: RepurchaseSortFilterUiModel) {
        val filterApplied = data.sortFilterList.any {
            val selectedDateFilter = it.selectedDateFilter
            val startDate = selectedDateFilter?.startDate
            val endDate = selectedDateFilter?.endDate

            !it.selectedItem?.id.isNullOrEmpty() || it.sort != DEFAULT_SORT_FILTER ||
                !startDate.isNullOrEmpty() || !endDate.isNullOrEmpty()
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
        val gridLayoutParams = binding?.sortFilter?.layoutParams as? GridLayoutManager.LayoutParams
        gridLayoutParams?.marginStart = marginStart
        binding?.sortFilter?.layoutParams = gridLayoutParams
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
        binding?.sortFilter?.resetAllFilters()
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
