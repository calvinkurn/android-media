package com.tokopedia.power_merchant.subscribe.common.constant

import androidx.annotation.StringDef
import com.tokopedia.gm.common.constant.PMShopGrade

/**
 * Created By @ilhamsuaib on 04/03/21
 */

@Retention(AnnotationRetention.SOURCE)
@StringDef(value = [PMShopGrade.NO_GRADE, PMShopGrade.BRONZE, PMShopGrade.SILVER, PMShopGrade.GOLD, PMShopGrade.DIAMOND])
annotation class ShopGrade
