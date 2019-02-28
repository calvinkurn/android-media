package com.tokopedia.normalcheckout.constant

import android.support.annotation.IntDef

@IntDef(ATC_ONLY, ATC_AND_BUY, ATC_AND_SELECT)
@Retention(AnnotationRetention.SOURCE)
annotation class ProductAction {}

const val ATC_ONLY = 0
const val ATC_AND_BUY = 1 // or preorder
const val ATC_AND_SELECT = 2