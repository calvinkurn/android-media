package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory
import com.tokopedia.sortfilter.SortFilterItem

data class ItemDetailPenaltyFilterUiModel(var periodDetail: String = "",
                                          var itemSortFilterWrapperList: List<ItemSortFilterWrapper> = listOf()): BasePenaltyPage {
    data class ItemSortFilterWrapper(
            val sortFilterItem: SortFilterItem? = null,
            var isSelected: Boolean = false,
            var value: String = ""
    )

    override fun type(typeFactory: PenaltyPageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}