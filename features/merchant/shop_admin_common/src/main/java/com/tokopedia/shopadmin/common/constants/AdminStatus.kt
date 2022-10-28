package com.tokopedia.shopadmin.common.constants

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class AdminStatus {
    companion object {
        const val ACTIVE = "1"
        const val WAITING_CONFIRMATION = "8"
        const val REJECT = "9"
        const val EXPIRED = "10"
    }
}