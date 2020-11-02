package com.tokopedia.shop.settings.common.util

import androidx.annotation.StringDef
import com.tokopedia.shop.settings.common.util.ShopTypeDef.Companion.GOLD_MERCHANT
import com.tokopedia.shop.settings.common.util.ShopTypeDef.Companion.OFFICIAL_STORE
import com.tokopedia.shop.settings.common.util.ShopTypeDef.Companion.REGULAR_MERCHANT

@StringDef(OFFICIAL_STORE, GOLD_MERCHANT, REGULAR_MERCHANT)
@Retention(AnnotationRetention.SOURCE)
annotation class ShopTypeDef {
    companion object {
        const val OFFICIAL_STORE = "official_store";
        const val GOLD_MERCHANT = "gold_merchant";
        const val REGULAR_MERCHANT = "regular";
    }
}