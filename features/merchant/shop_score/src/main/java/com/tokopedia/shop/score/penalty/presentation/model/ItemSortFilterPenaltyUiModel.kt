package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory

data class ItemSortFilterPenaltyUiModel(var itemSortFilterWrapperList: List<ItemSortFilterWrapper> = listOf()): BasePenaltyPage {

    data class ItemSortFilterWrapper(
            val title: String = "",
            var isSelected: Boolean = false,
            var idFilter: Int = 0
    )

    override fun type(typeFactory: PenaltyPageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}