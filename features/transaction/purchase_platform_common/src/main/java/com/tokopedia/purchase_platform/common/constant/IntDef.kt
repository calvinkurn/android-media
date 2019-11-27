package com.tokopedia.purchase_platform.common.constant

import androidx.annotation.IntDef

@IntDef(ATC_ONLY, ATC_AND_BUY, TRADEIN_BUY, APPLY_CREDIT)
@Retention(AnnotationRetention.SOURCE)
annotation class ProductAction {}

const val ATC_ONLY = 0
const val ATC_AND_BUY = 1 // or preorder
const val TRADEIN_BUY = 2
const val APPLY_CREDIT = 3