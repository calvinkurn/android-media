package com.tokopedia.shop.common.constant

import androidx.annotation.IntDef

/**
 * @author normansyahputa on 4/25/17.
 */
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@IntDef(ShopScheduleActionDef.CLOSED, ShopScheduleActionDef.OPEN, ShopScheduleActionDef.ABORT)
annotation class ShopScheduleActionDef {
    companion object {
        const val CLOSED = 0
        const val OPEN = 1
        const val ABORT = 2
    }
}