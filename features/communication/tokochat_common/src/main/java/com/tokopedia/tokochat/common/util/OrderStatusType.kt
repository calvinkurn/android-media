package com.tokopedia.tokochat.common.util

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class OrderStatusType {
    companion object {

        const val TOKOFOOD_COMPLETED = "COMPLETED"
        const val TOKOFOOD_CANCELLED = "CANCELLED"
        const val LOGISTIC_COMPLETED = "pg_order_finished"
        const val LOGISTIC_CANCELLED = "pg_cancelled"
    }
}
