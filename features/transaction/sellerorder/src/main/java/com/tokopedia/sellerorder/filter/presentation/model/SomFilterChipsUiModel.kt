package com.tokopedia.sellerorder.filter.presentation.model

data class SomFilterChipsUiModel(
        val idStatus: List<Int> = listOf(),
        val id: Int = 0,
        val name: String = "",
        var isSelected: Boolean = false,
        val childStatus: List<ChildStatusUiModel> = listOf()
) {

    data class ChildStatusUiModel(
            val childId: List<Int> = listOf(),
            val key: String = "",
            val text: String = "",
            val isChecked: Boolean = false
    )
}