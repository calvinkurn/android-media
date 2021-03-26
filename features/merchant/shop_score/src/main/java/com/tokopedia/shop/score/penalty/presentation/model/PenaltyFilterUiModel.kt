package com.tokopedia.shop.score.penalty.presentation.model

data class PenaltyFilterUiModel(
        val title: String = "",
        val isDividerVisible: Boolean = false,
        val canSelectMany: Boolean = false,
        val chipsFilerList: List<ChipsFilterPenaltyUiModel> = listOf()
) {
    data class ChipsFilterPenaltyUiModel(
        val title: String = "",
        // temporary
        val value: String = "",
        val isSelected: Boolean = false
    )
}