package com.tokopedia.sellerorder.list.presentation.models

import com.tokopedia.kotlin.extensions.view.isMoreThanZero

data class SomListFilterUiModel(
        val statusList: List<Status> = listOf(),
        val orderTypeList: List<OrderType> = listOf(),
        var refreshOrder: Boolean = true,
        val fromCache: Boolean) {

    fun mergeWithCurrent(
        currentSomListFilterData: SomListFilterUiModel?,
        orderTypeFilterFromAppLink: Long
    ) {
        orderTypeList.forEach { orderType ->
            orderType.isChecked = currentSomListFilterData?.orderTypeList?.find {
                it.key == orderType.key
            }?.isChecked ?: orderType.isChecked
        }
        if (currentSomListFilterData == null && orderTypeFilterFromAppLink.isMoreThanZero()) {
            orderTypeList.find { it.id == orderTypeFilterFromAppLink }?.isChecked = true
        }
    }

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
    data class OrderType(
        val id: Long = 0L,
        val key: String = "",
        val name: String = "",
        var isChecked: Boolean = false
    )
}