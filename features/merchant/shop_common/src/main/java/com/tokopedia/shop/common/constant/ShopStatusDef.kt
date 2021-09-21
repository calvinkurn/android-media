package com.tokopedia.shop.common.constant

import androidx.annotation.IntDef

/**
 * @author normansyahputa on 4/25/17.
 */
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@IntDef(ShopStatusDef.OPEN, ShopStatusDef.NOT_ACTIVE, ShopStatusDef.CLOSED, ShopStatusDef.DELETED, ShopStatusDef.MODERATED, ShopStatusDef.MODERATED_PERMANENTLY, ShopStatusDef.INCUBATED, ShopStatusDef.INCOMPLETE)
annotation class
ShopStatusDef {
    companion object {
        const val DELETED = 0
        const val OPEN = 1
        const val CLOSED = 2
        const val MODERATED = 3
        const val NOT_ACTIVE = 4    // Inactive
        const val MODERATED_PERMANENTLY = 5
        const val INCUBATED = 6
        const val INCOMPLETE = 7
    }
}