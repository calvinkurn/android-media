package com.tokopedia.merchantvoucher.common.constant

import androidx.annotation.IntDef
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherTypeDef.Companion.TYPE_CASHBACK
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherTypeDef.Companion.TYPE_DISCOUNT
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherTypeDef.Companion.TYPE_FREE_ONGKIR



@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [TYPE_FREE_ONGKIR, TYPE_DISCOUNT, TYPE_CASHBACK])
annotation class MerchantVoucherTypeDef {
    companion object {
        const val TYPE_FREE_ONGKIR = 1
        const val TYPE_DISCOUNT = 2
        const val TYPE_CASHBACK = 3
    }
}

