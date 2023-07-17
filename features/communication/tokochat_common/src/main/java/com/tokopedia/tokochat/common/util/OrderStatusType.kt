package com.tokopedia.tokochat.common.util

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class OrderStatusType {
    companion object {
        const val NEW = "NEW"
        const val CREATED = "CREATED"
        const val AWAITING_MERCHANT_ACCEPTANCE = "AWAITING_MERCHANT_ACCEPTANCE"
        const val MERCHANT_ACCEPTED = "MERCHANT_ACCEPTED"
        const val SEARCHING_DRIVER = "SEARCHING_DRIVER"
        const val OTW_PICKUP = "OTW_PICKUP"
        const val DRIVER_ARRIVED = "DRIVER_ARRIVED"
        const val PICKUP_REQUESTED = "PICKUP_REQUESTED"
        const val ORDER_PLACED = "ORDER_PLACED"
        const val OTW_DESTINATION = "OTW_DESTINATION"
        const val FRAUD_CHECK = "FRAUD_CHECK"
        const val COMPLETED = "COMPLETED"
        const val CANCELLED = "CANCELLED"
    }
}
