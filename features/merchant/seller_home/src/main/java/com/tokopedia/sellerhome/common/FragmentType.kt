package com.tokopedia.sellerhome.common

import androidx.annotation.IntDef

/**
 * Created By @ilhamsuaib on 2020-03-05
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@IntDef(value = [
    FragmentType.HOME, FragmentType.PRODUCT,
    FragmentType.CHAT, FragmentType.ORDER,
    FragmentType.OTHER
])
annotation class FragmentType {
    companion object {
        const val HOME: Int = 0
        const val PRODUCT: Int = 1
        const val CHAT: Int = 2
        const val ORDER: Int = 3
        const val OTHER: Int = 4
    }
}