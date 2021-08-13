package com.tokopedia.shop.common.constant

import androidx.annotation.IntDef

/**
 * @author normansyahputa on 4/25/17.
 */
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@IntDef(ShopStatusLevelDef.LEVEL_REGULAR, ShopStatusLevelDef.LEVEL_GOLD, ShopStatusLevelDef.LEVEL_OFFICIAL_STORE)
annotation class ShopStatusLevelDef {
    companion object {
        const val LEVEL_REGULAR = 0
        const val LEVEL_GOLD = 1
        const val LEVEL_OFFICIAL_STORE = 2
    }
}