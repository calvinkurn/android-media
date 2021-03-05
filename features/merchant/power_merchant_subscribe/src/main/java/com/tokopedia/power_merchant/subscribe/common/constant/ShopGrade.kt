package com.tokopedia.power_merchant.subscribe.common.constant

import androidx.annotation.IntDef

/**
 * Created By @ilhamsuaib on 04/03/21
 */

@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [ShopGrade.BRONZE, ShopGrade.SILVER, ShopGrade.GOLD, ShopGrade.DIAMOND])
annotation class ShopGrade {

    companion object {
        const val BRONZE = 1
        const val SILVER = 2
        const val GOLD = 3
        const val DIAMOND = 4
    }
}
