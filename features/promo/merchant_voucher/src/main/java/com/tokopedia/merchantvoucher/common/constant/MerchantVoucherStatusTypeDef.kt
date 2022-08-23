package com.tokopedia.merchantvoucher.common.constant

import androidx.annotation.IntDef


import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef.Companion.TYPE_AVAILABLE
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef.Companion.TYPE_IN_USE
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef.Companion.TYPE_RESTRICTED
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef.Companion.TYPE_RUN_OUT


@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [TYPE_AVAILABLE, TYPE_IN_USE, TYPE_RUN_OUT, TYPE_RESTRICTED])
annotation class MerchantVoucherStatusTypeDef{
    companion object{
       const val TYPE_IN_USE = 1
       const val TYPE_AVAILABLE = 2
       const val TYPE_RUN_OUT = 3
       const val TYPE_RESTRICTED = 4
    }
}


