package com.tokopedia.power_merchant.subscribe.common.constant

import androidx.annotation.StringDef
import com.tokopedia.gm.common.constant.PMShopGrade

/**
 * Created By @ilhamsuaib on 04/03/21
 */

@Retention(AnnotationRetention.SOURCE)
@StringDef(value = [PMShopGrade.ADVANCED, PMShopGrade.EXPERT, PMShopGrade.ULTIMATE])
annotation class ShopGrade
