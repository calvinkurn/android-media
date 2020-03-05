package com.tokopedia.sellerhome.common

import androidx.annotation.IntDef

/**
 * Created By @ilhamsuaib on 2020-03-05
 */

@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [
    FragmentType.HOME, FragmentType.PRODUCT,
    FragmentType.CHAT, FragmentType.ORDER
])
annotation class FragmentType {
    companion object {
        const val HOME = 0
        const val PRODUCT = 1
        const val CHAT = 2
        const val ORDER = 3
        const val OTHER = 4
    }
}