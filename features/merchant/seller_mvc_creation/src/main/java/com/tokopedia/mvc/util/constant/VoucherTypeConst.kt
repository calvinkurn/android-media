package com.tokopedia.mvc.util.constant

import androidx.annotation.IntDef

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(VoucherTypeConst.FREE_ONGKIR, VoucherTypeConst.DISCOUNT, VoucherTypeConst.CASHBACK)
annotation class VoucherTypeConst {

    companion object {
        const val FREE_ONGKIR = 1
        const val DISCOUNT = 2
        const val CASHBACK = 3
    }
}
