package com.tokopedia.shopdiscount.utils.constant

import androidx.annotation.IntDef

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(DiscountStatus.SCHEDULED, DiscountStatus.ONGOING, DiscountStatus.PAUSED)
annotation class DiscountStatus {
    companion object {
        const val ALL = 0
        const val SCHEDULED = 1
        const val ONGOING = 2
        const val PAUSED = 4
    }
}
