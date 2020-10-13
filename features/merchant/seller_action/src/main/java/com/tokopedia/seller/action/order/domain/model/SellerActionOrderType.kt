package com.tokopedia.seller.action.order.domain.model

import androidx.annotation.StringDef

@StringDef(
        SellerActionOrderType.ORDER_CANCELLED, SellerActionOrderType.ORDER_DELIVERED, SellerActionOrderType.ORDER_IN_TRANSIT,
        SellerActionOrderType.ORDER_PAYMENT_DUE, SellerActionOrderType.ORDER_PICKUP_AVAILABLE, SellerActionOrderType.ORDER_PROBLEM,
        SellerActionOrderType.ORDER_PROCESSING, SellerActionOrderType.ORDER_RETURNED, SellerActionOrderType.ORDER_DEFAULT)
@Retention(AnnotationRetention.SOURCE)
annotation class SellerActionOrderType {
    companion object {
        const val ORDER_CANCELLED = "OrderCancelled"
        const val ORDER_DELIVERED = "OrderDelivered"
        const val ORDER_IN_TRANSIT = "OrderInTransit"
        const val ORDER_PAYMENT_DUE = "OrderPaymentDue"
        const val ORDER_PICKUP_AVAILABLE = "OrderPickupAvailable"
        const val ORDER_PROBLEM = "OrderProblem"
        const val ORDER_PROCESSING = "OrderProcessing"
        const val ORDER_RETURNED = "OrderReturned"
        const val ORDER_DEFAULT = "order_default"
    }
}