package com.tokopedia.inboxcommon

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@IntDef(
    value = [
        RoleType.BUYER,
        RoleType.SELLER,
        RoleType.AFFILIATE
    ]
)
annotation class RoleType {
    companion object {
        const val BUYER: Int = 1
        const val SELLER: Int = 2
        const val AFFILIATE: Int = 6
    }
}
