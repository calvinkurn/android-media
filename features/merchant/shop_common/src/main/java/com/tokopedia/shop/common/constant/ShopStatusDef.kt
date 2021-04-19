package com.tokopedia.shop.common.constant

import androidx.annotation.IntDef

/**
 * @author normansyahputa on 4/25/17.
 */
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@IntDef(ShopStatusDef.OPEN, ShopStatusDef.NOT_ACTIVE, ShopStatusDef.CLOSED, ShopStatusDef.DELETED, ShopStatusDef.MODERATED, ShopStatusDef.MODERATED_PERMANENTLY)
annotation class ShopStatusDef {
    companion object {
        const val OPEN = 1
        const val NOT_ACTIVE = 4
        const val CLOSED = 2
        const val DELETED = 0
        const val MODERATED = 3
        const val MODERATED_PERMANENTLY = 5
    }
}