package com.tokopedia.tokomart.home.constant

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    HomeLayoutType.SECTION,
    HomeLayoutType.ALL_CATEGORY,
    HomeLayoutType.DYNAMIC_CHANNEL
)
annotation class HomeLayoutType {
    companion object {
        const val SECTION = 0
        const val ALL_CATEGORY = 1
        const val DYNAMIC_CHANNEL = 2
    }
}