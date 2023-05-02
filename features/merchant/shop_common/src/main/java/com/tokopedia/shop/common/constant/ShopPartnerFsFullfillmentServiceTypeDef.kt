package com.tokopedia.shop.common.constant

import androidx.annotation.IntDef

/**
 * Docs: https://tokopedia.atlassian.net/wiki/spaces/TISR/pages/1064599650/Get+Partner+Shop+Label
 */

@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@IntDef(ShopPartnerFsFullfillmentServiceTypeDef.DEFAULT, ShopPartnerFsFullfillmentServiceTypeDef.TOKO_CABANG, ShopPartnerFsFullfillmentServiceTypeDef.EPHARMACY, ShopPartnerFsFullfillmentServiceTypeDef.NEW_RETAIL, ShopPartnerFsFullfillmentServiceTypeDef.TOKONOW, ShopPartnerFsFullfillmentServiceTypeDef.B2B2C)
annotation class
ShopPartnerFsFullfillmentServiceTypeDef {
    companion object {
        const val DEFAULT = 0
        const val TOKO_CABANG = 1
        const val EPHARMACY = 2
        const val NEW_RETAIL = 3
        const val TOKONOW = 4
        const val B2B2C = 5
    }
}
