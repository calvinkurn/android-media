package com.tokopedia.shop.score.penalty.presentation.model

data class FilterTypePenaltyUiModelWrapper(
        val itemFilterTypePenalty: List<ItemFilterTypePenalty> = listOf(),
        val sortBy: Pair<String, Boolean>? = null
) {
    data class ItemFilterTypePenalty(
            val isSelected: Boolean = false,
            val title: String = ""
    )
}