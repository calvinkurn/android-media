package com.tokopedia.notifcenter.common

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@IntDef(value = [
    NotificationFilterType.NONE,
    NotificationFilterType.TRANSACTION,
    NotificationFilterType.PROMOTION,
    NotificationFilterType.INFO
])
annotation class NotificationFilterType {
    companion object {
        const val NONE: Int = 0
        const val TRANSACTION: Int = 12
        const val PROMOTION: Int = 2
        const val INFO: Int = 1
    }
}