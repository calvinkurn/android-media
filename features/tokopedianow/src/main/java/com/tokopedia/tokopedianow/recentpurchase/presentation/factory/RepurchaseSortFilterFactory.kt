package com.tokopedia.tokopedianow.recentpurchase.presentation.factory

import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recentpurchase.constant.RepurchaseStaticLayoutId.Companion.SORT_FILTER
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import com.tokopedia.unifycomponents.ChipsUnify

object RepurchaseSortFilterFactory {

    fun createSortFilter(): RepurchaseSortFilterUiModel {
        val sortFilterList = mutableListOf<RepurchaseSortFilter>().apply {
            val sortFilter = RepurchaseSortFilter(
                title = R.string.tokopedianow_repurchase_sort_chip_title,
                type = RepurchaseSortFilterType.SORT
            )

            val categoryFilter = RepurchaseSortFilter(
                title = R.string.tokopedianow_repurchase_all_category_filter_chip_title,
                qtyFormat = R.string.tokopedianow_repurchase_category_filter_chip_format,
                type = RepurchaseSortFilterType.CATEGORY_FILTER
            )

            val dateFilter = RepurchaseSortFilter(
                title = R.string.tokopedianow_repurchase_date_filter_chip_title,
                type = RepurchaseSortFilterType.DATE_FILTER
            )

            add(sortFilter)
            add(categoryFilter)
            add(dateFilter)
        }

        return RepurchaseSortFilterUiModel(SORT_FILTER, sortFilterList)
    }
}