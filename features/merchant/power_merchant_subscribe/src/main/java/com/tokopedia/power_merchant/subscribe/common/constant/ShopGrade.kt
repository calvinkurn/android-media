package com.tokopedia.power_merchant.subscribe.common.constant

import androidx.annotation.StringDef

/**
 * Created By @ilhamsuaib on 04/03/21
 */

@Retention(AnnotationRetention.SOURCE)
@StringDef(value = [ShopGrade.UNDEFINED, ShopGrade.BRONZE, ShopGrade.SILVER, ShopGrade.GOLD, ShopGrade.DIAMOND])
annotation class ShopGrade {

    companion object {
        const val UNDEFINED = "undefined"
        const val BRONZE = "Bronze"
        const val SILVER = "Silver"
        const val GOLD = "Gold"
        const val DIAMOND = "Diamond"
    }
}
