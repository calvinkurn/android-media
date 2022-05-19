package com.tokopedia.campaignlist.common.constant

import androidx.annotation.StringDef
import com.tokopedia.campaignlist.common.constant.ShopTypeDef.Companion.OFFICIAL_STORE
import com.tokopedia.campaignlist.common.constant.ShopTypeDef.Companion.POWER_MERCHANT
import com.tokopedia.campaignlist.common.constant.ShopTypeDef.Companion.POWER_MERCHANT_PRO
import com.tokopedia.campaignlist.common.constant.ShopTypeDef.Companion.REGULAR_MERCHANT


@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(POWER_MERCHANT, POWER_MERCHANT_PRO, OFFICIAL_STORE, REGULAR_MERCHANT)
annotation class ShopTypeDef {
    companion object {
        const val POWER_MERCHANT = "pm"
        const val POWER_MERCHANT_PRO = "pro"
        const val OFFICIAL_STORE = "official"
        const val REGULAR_MERCHANT = "none"
    }
}
