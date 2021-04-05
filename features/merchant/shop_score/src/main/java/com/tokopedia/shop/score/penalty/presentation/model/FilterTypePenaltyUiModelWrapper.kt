package com.tokopedia.shop.score.penalty.presentation.model

data class FilterTypePenaltyUiModelWrapper(
        val itemFilterTypePenalty: List<ItemFilterTypePenalty> = listOf()
) {
    data class ItemFilterTypePenalty(
            val isSelected: Boolean = false,
            val title: String = ""
    )
}