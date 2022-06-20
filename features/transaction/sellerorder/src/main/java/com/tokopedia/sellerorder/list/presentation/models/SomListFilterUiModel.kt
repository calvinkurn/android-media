package com.tokopedia.sellerorder.list.presentation.models

import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam

data class SomListFilterUiModel(
    val statusList: List<Status> = listOf(),
    val orderTypeList: List<OrderType> = listOf(),
    val sortByList: List<SortBy> = listOf(),
    var refreshOrder: Boolean = true,
    val fromCache: Boolean
) {

    fun mergeWithCurrent(getOrderListParam: SomListGetOrderListParam, tabActiveFromAppLink: String) {
        if (tabActiveFromAppLink.isNotBlank()) {
            statusList.find {
                it.key == tabActiveFromAppLink
            }?.run {
                isChecked = true
                childStatuses.forEach { childStatus ->
                    childStatus.isChecked = true
                }
                getOrderListParam.statusList = id
            }
        } else {
            statusList.forEach { status ->
                status.isChecked = if (status.key == SomConsts.STATUS_ALL_ORDER) {
                    getOrderListParam.statusList.isEmpty()
                } else {
                    getOrderListParam.statusList.any {
                        it in status.id
                    }
                }
                status.childStatuses.forEach { childStatus ->
                    childStatus.isChecked = getOrderListParam.statusList.any {
                        it in childStatus.id
                    }
                }
            }
        }
        orderTypeList.forEach { orderType ->
            orderType.isChecked = getOrderListParam.orderTypeList.contains(orderType.id)
        }
        sortByList.forEach { sortBy ->
            sortBy.isChecked = getOrderListParam.sortBy == sortBy.id
        }
    }

    fun getSelectedOrderStatusIds(): List<Int> {
        return statusList.find { it.isChecked }?.id.orEmpty()
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

    data class SortBy(
        val id: Long = 0L,
        var isChecked: Boolean = false
    )
}