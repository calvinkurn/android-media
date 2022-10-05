package com.tokopedia.merchantvoucher.common.constant

import androidx.annotation.IntDef


@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [MerchantVoucherOwnerTypeDef.TYPE_MERCHANT, MerchantVoucherOwnerTypeDef.TYPE_TOKOPEDIA])
annotation class MerchantVoucherOwnerTypeDef{
    companion object{
        const val TYPE_MERCHANT = 1
        const val TYPE_TOKOPEDIA = 2
    }
}
