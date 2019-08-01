package com.tokopedia.purchase_platform.common

import android.support.annotation.IntDef

@IntDef(ATC_ONLY, ATC_AND_BUY, TRADEIN_BUY)
@Retention(AnnotationRetention.SOURCE)
annotation class ProductAction {}

const val ATC_ONLY = 0
const val ATC_AND_BUY = 1 // or preorder
const val TRADEIN_BUY = 2