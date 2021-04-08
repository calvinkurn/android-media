package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.sortfilter.SortFilterItem

data class ItemDetailPenaltyFilterUiModel(var periodDetail: String = "",
                                          var itemSortFilterWrapperList: List<ItemSortFilterWrapper> = listOf()) {
    data class ItemSortFilterWrapper(
            val sortFilterItem: SortFilterItem? = null,
            var isSelected: Boolean = false,
            var value: String = ""
    )
}