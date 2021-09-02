package com.tokopedia.tokopedianow.recentpurchase.presentation.factory

import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import com.tokopedia.unifycomponents.ChipsUnify

object RepurchaseSortFilterFactory {

    fun createSortFilterList(): List<RepurchaseSortFilter> {
        return mutableListOf<RepurchaseSortFilter>().apply {
            val sortFilter = RepurchaseSortFilter(
                title = R.string.tokopedianow_repurchase_sort_chip_title,
                chipType = ChipsUnify.TYPE_SELECTED,
                filterType = RepurchaseSortFilterType.SORT
            )

            val categoryFilter = RepurchaseSortFilter(
                title = R.string.tokopedianow_repurchase_category_filter_chip_title,
                filterType = RepurchaseSortFilterType.CATEGORY_FILTER
            )

            val dateFilter = RepurchaseSortFilter(
                title = R.string.tokopedianow_repurchase_date_filter_chip_title,
                filterType = RepurchaseSortFilterType.DATE_FILTER
            )

            add(sortFilter)
            add(categoryFilter)
            add(dateFilter)
        }
    }
}