package com.tokopedia.tokopedianow.repurchase.presentation.factory

import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.SORT_FILTER
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.Companion.FREQUENTLY_BOUGHT

object RepurchaseSortFilterFactory {

    fun createSortFilter(): RepurchaseSortFilterUiModel {
        val sortFilterList = mutableListOf<RepurchaseSortFilter>().apply {
            val sortFilter = RepurchaseSortFilter(
                title = R.string.tokopedianow_repurchase_sort_chip_title,
                type = RepurchaseSortFilterType.SORT,
                sort = FREQUENTLY_BOUGHT
            )

            val categoryFilter = RepurchaseSortFilter(
                title = R.string.tokopedianow_repurchase_all_category_filter_chip_title,
                titleFormat = R.string.tokopedianow_repurchase_category_filter_chip_format,
                type = RepurchaseSortFilterType.CATEGORY_FILTER
            )

            val dateFilter = RepurchaseSortFilter(
                title = R.string.tokopedianow_repurchase_date_filter_chip_title,
                type = RepurchaseSortFilterType.DATE_FILTER,
                selectedDateFilter = SelectedDateFilter()
            )

            add(sortFilter)
            add(categoryFilter)
            add(dateFilter)
        }

        return RepurchaseSortFilterUiModel(SORT_FILTER, sortFilterList)
    }
}