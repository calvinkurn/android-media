package com.tokopedia.merchantvoucher.common.constant

import androidx.annotation.IntDef



@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [MerchantVoucherAmountTypeDef.TYPE_FIXED, MerchantVoucherAmountTypeDef.TYPE_PERCENTAGE])
annotation class MerchantVoucherAmountTypeDef{
    companion object{
        const val TYPE_FIXED = 1
        const val TYPE_PERCENTAGE = 2
    }
}
