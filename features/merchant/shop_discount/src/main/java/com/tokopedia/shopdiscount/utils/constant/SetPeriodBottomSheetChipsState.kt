package com.tokopedia.shopdiscount.utils.constant

import androidx.annotation.IntDef
import com.tokopedia.shopdiscount.utils.constant.SetPeriodBottomSheetChipsState.Companion.CUSTOM
import com.tokopedia.shopdiscount.utils.constant.SetPeriodBottomSheetChipsState.Companion.ONE_MONTH
import com.tokopedia.shopdiscount.utils.constant.SetPeriodBottomSheetChipsState.Companion.ONE_YEAR
import com.tokopedia.shopdiscount.utils.constant.SetPeriodBottomSheetChipsState.Companion.SIX_MONTH

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(ONE_YEAR, SIX_MONTH, ONE_MONTH, CUSTOM)
annotation class SetPeriodBottomSheetChipsState {
    companion object {
        const val ONE_YEAR = 1
        const val SIX_MONTH = 2
        const val ONE_MONTH = 3
        const val CUSTOM = 4
    }
}