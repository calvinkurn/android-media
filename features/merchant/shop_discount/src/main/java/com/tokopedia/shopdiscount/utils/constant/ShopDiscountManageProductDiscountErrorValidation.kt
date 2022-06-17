package com.tokopedia.shopdiscount.utils.constant

import androidx.annotation.IntDef
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_PRICE_MAX
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_PRICE_MIN
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.NONE

@Retention(AnnotationRetention.SOURCE)
@IntDef(NONE, ERROR_PRICE_MAX, ERROR_PRICE_MIN)
annotation class ShopDiscountManageProductDiscountErrorValidation {
    companion object {
        const val NONE = 0
        const val ERROR_PRICE_MAX = 1
        const val ERROR_PRICE_MIN = 2
        const val ERROR_R2_ABUSIVE = 3
        const val ERROR_START_DATE = 4
    }
}
