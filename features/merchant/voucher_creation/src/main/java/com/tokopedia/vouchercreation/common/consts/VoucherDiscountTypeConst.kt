package com.tokopedia.vouchercreation.common.consts

import androidx.annotation.IntDef

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(VoucherDiscountTypeConst.IDR, VoucherDiscountTypeConst.PERCENTAGE)
annotation class VoucherDiscountTypeConst {

    companion object {
        const val IDR = 1
        const val PERCENTAGE = 2
    }
}