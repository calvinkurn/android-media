package com.tokopedia.shop.score.penalty.presentation.model

data class ItemDetailPenaltyFilterUiModel(var periodDetail: String = "",
                                          var itemSortFilterWrapperList: List<ItemSortFilterWrapper> = listOf()) {
    data class ItemSortFilterWrapper(
            val title: String = "",
            var isSelected: Boolean = false,
            var idFilter: Int = 0
    )
}