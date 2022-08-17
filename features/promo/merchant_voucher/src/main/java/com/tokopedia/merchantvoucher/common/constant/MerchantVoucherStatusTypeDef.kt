package com.tokopedia.merchantvoucher.common.constant

import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef.TYPE_AVAILABLE


@Retention(RetentionPolicy.SOURCE)
@IntDef({})
annotation class MerchantVoucherStatusTypeDef(
    val TYPE_IN_USE:Int = 1,
    val TYPE_AVAILABLE:Int = 2,
    val TYPE_RUN_OUT:Int = 3,
    val TYPE_RESTRICTED:Int = 4,
)


