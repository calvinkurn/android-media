package com.tokopedia.sellerorder.list.presentation.models

import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam

// mappers -> loadFilters -> mergeWithCurrent -> Update Tabs -> select tab -> loadFilters
data class SomListFilterUiModel(
    val statusList: List<Status> = listOf(),
    val sortByList: List<SortBy> = listOf(),
    var refreshOrder: Boolean = true,
    val fromCache: Boolean,
    val quickFilterList: List<QuickFilter> = listOf(),
    val highLightedStatusKey: String
) {

    fun mergeWithCurrent(
        getOrderListParam: SomListGetOrderListParam,
        tabActiveFromAppLink: String,
        isFirstPageOpened: Boolean
    ) {
        if (tabActiveFromAppLink.isNotBlank()) {
            statusList.find {
                it.key == tabActiveFromAppLink
            }?.run {
                isChecked = true
                childStatuses.forEach { childStatus ->
                    childStatus.isChecked = true
                }
                getOrderListParam.statusKey = key
                getOrderListParam.statusList = id
            }
        }
        // highlightedStatusKey: new order, all order, confirm_shipping when it first load
        else if (isEnableAutoTabbing(highLightedStatusKey, isFirstPageOpened)) {
            statusList.find {
                it.key == highLightedStatusKey
            }?.run {
                isChecked = true
                childStatuses.forEach { childStatus ->
                    childStatus.isChecked = true
                }
                getOrderListParam.statusKey = key
                getOrderListParam.statusList = id
            }
        } else {
            statusList.forEach { status ->
                if (status.key == SomConsts.STATUS_ALL_ORDER) {
                    status.isChecked = getOrderListParam.statusList.isEmpty()
                } else {
                    status.isChecked = getOrderListParam.statusList.any {
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
        sortByList.forEach { sortBy ->
            sortBy.isChecked = getOrderListParam.sortBy == sortBy.id
        }
        quickFilterList.forEach { quickFilter ->
            quickFilter.isChecked = when {
                quickFilter.isOrderTypeFilter() -> {
                    getOrderListParam.orderTypeList.contains(quickFilter.id)
                }

                quickFilter.isShippingFilter() -> {
                    getOrderListParam.shippingList.contains(quickFilter.id)
                }

                else -> quickFilter.isChecked
            }
        }
    }

    private fun isEnableAutoTabbing(highLightedStatusKey: String, isFirstPageOpened: Boolean): Boolean {
        return highLightedStatusKey.isNotBlank() && isFirstPageOpened && Utils.isEnableOperationalGuideline()
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

    data class QuickFilter(
        val id: Long = 0L,
        val key: String = "",
        val name: String = "",
        val type: String = "",
        var isChecked: Boolean = false
    ) {

        companion object {
            private const val TYPE_ORDER_TYPE = "order_type"
            private const val TYPE_SHIPPING = "shipping"
        }

        fun isOrderTypeFilter(): Boolean {
            return type == TYPE_ORDER_TYPE
        }

        fun isShippingFilter(): Boolean {
            return type == TYPE_SHIPPING
        }
    }
}
