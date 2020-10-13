package com.tokopedia.seller.action.order.domain.mapper

import com.tokopedia.seller.action.order.domain.model.SellerActionOrderCode
import com.tokopedia.seller.action.order.domain.model.SellerActionOrderType

object SellerActionOrderCodeMapper {
    internal fun mapOrderCodeByType(@SellerActionOrderType orderType: String): List<Int> {
        return when(orderType) {
            SellerActionOrderType.ORDER_CANCELLED -> SellerActionOrderCode.STATUS_CODE_ORDER_CANCELLED
            SellerActionOrderType.ORDER_PROCESSING -> listOf(SellerActionOrderCode.STATUS_CODE_ORDER_CREATED)
            SellerActionOrderType.ORDER_PICKUP_AVAILABLE -> listOf(SellerActionOrderCode.STATUS_CODE_ORDER_PICKUP_AVAILABLE)
            SellerActionOrderType.ORDER_PROBLEM -> SellerActionOrderCode.STATUS_CODE_ORDER_PROBLEM
            SellerActionOrderType.ORDER_RETURNED -> listOf(SellerActionOrderCode.STATUS_CODE_ORDER_RETURNED)
            SellerActionOrderType.ORDER_IN_TRANSIT -> SellerActionOrderCode.STATUS_CODE_ORDER_IN_DELIVERY
            SellerActionOrderType.ORDER_DELIVERED -> SellerActionOrderCode.STATUS_CODE_ORDER_FINISHED
            else -> SellerActionOrderCode.STATUS_CODE_DEFAULT
        }
    }
}