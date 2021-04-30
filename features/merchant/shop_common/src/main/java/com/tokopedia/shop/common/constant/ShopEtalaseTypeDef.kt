package com.tokopedia.shop.common.constant

import androidx.annotation.IntDef

/**
 * @author normansyahputa on 4/25/17.
 */
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@IntDef(ShopEtalaseTypeDef.ETALASE_DEFAULT, ShopEtalaseTypeDef.ETALASE_CUSTOM)
annotation class ShopEtalaseTypeDef {
    companion object {
        const val ETALASE_DEFAULT = -1 // Generated etalase
        const val ETALASE_CAMPAIGN = -2
        const val ETALASE_THEMATIC_CAMPAIGN = -3
        const val ETALASE_CUSTOM = 1
    }
}