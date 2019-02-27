package com.tokopedia.normalcheckout.constant

import android.support.annotation.IntDef

@IntDef(ATC_ONLY, ATC_AND_BUY, ATC_AND_SELECT, PREORDER)
@Retention(AnnotationRetention.SOURCE)
annotation class ProductAction {}

const val ATC_ONLY = 0
const val ATC_AND_BUY = 1
const val ATC_AND_SELECT = 2
const val PREORDER = 3