package com.tokopedia.sellerorder.list.presentation.models

data class SomListFilterUiModel(
        val statusList: List<Status> = listOf(),
        var refreshOrder: Boolean = true,
        val fromCache: Boolean) {
    data class Status(
            val key: String = "",
            val status: String = "",
            var amount: Int = 0,
            val id: List<Int> = listOf(),
            val childStatuses: List<ChildStatus> = listOf(),
            var isChecked: Boolean = false
    ) {
        data class ChildStatus(
                val key: String = "",
                val status: String = "",
                val amount: Int = 0,
                val id: List<Int> = listOf(),
                var isChecked: Boolean = false
        )
    }
}