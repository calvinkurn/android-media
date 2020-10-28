package com.tokopedia.sellerorder.filter.presentation.model

data class SomFilterChipsUiModel(
        val idStatus: List<Int> = listOf(),
        val id: Int = 0,
        val name: String = "", //equal order status
        val key: String = "", //equal key of status_list
        var isSelected: Boolean = false,
        val amount: Int = 0,
        val childStatus: List<ChildStatusUiModel> = listOf()
) {
    data class ChildStatusUiModel(
            val childId: List<Int> = listOf(),
            val key: String = "",
            val text: String = "",
            val isChecked: Boolean = false
    )
}